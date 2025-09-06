package com.mycompany.gestaodeprojetosv2.controller;

import com.mycompany.gestaodeprojetosv2.model.Team;
import com.mycompany.gestaodeprojetosv2.model.Usuario;
import com.mycompany.gestaodeprojetosv2.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

    public void save(Team team) {
        String sql = "INSERT INTO teams (name, description) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, team.getName());
            stmt.setString(2, team.getDescription());
            stmt.execute();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                team.setId(rs.getInt(1));
            }

            // Salva os membros da equipe
            if (team.getMembers() != null && !team.getMembers().isEmpty()) {
                saveTeamMembers(team);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao salvar a equipe. " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }
    }
    
    public void update(Team team) {
        String sql = "UPDATE teams SET name = ?, description = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, team.getName());
            stmt.setString(2, team.getDescription());
            stmt.setInt(3, team.getId());
            stmt.execute();
            
            // Atualiza os membros da equipe
            updateTeamMembers(team);
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao atualizar a equipe. " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }
    }

    public void removeById(int teamId) {
        String sql = "DELETE FROM teams WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, teamId);
            stmt.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao deletar a equipe. " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }
    }
    
    private void saveTeamMembers(Team team) throws SQLException {
        String sql = "INSERT INTO team_members (team_id, user_id) VALUES (?, ?)";
        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for (Usuario member : team.getMembers()) {
            stmt.setInt(1, team.getId());
            stmt.setInt(2, member.getId());
            stmt.addBatch(); // Adiciona ao lote
        }
        stmt.executeBatch(); // Executa o lote de inserções
        ConnectionFactory.closeConnection(conn, stmt);
    }
    
    private void updateTeamMembers(Team team) throws SQLException {
        String deleteSql = "DELETE FROM team_members WHERE team_id = ?";
        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
        deleteStmt.setInt(1, team.getId());
        deleteStmt.execute();
        
        if (team.getMembers() != null && !team.getMembers().isEmpty()) {
            saveTeamMembers(team);
        }
    }

    public List<Team> getAll() {
        String sql = "SELECT * FROM teams";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Team> teams = new ArrayList<>();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Team team = new Team();
                team.setId(rs.getInt("id"));
                team.setName(rs.getString("name"));
                team.setDescription(rs.getString("description"));
                
                // Carrega os membros da equipe
                team.setMembers(getTeamMembers(team.getId()));
                
                teams.add(team);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar as equipes. " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }
        return teams;
    }
    
    public List<Usuario> getTeamMembers(int teamId) {
        String sql = "SELECT u.* FROM users u INNER JOIN team_members tm ON u.id = tm.user_id WHERE tm.team_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Usuario> members = new ArrayList<>();
        
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, teamId);
            rs = stmt.executeQuery();
            
            while(rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNomeCompleto(rs.getString("fullName"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCargo(rs.getString("role"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("password"));
                usuario.setPerfil(rs.getString("profile"));
                members.add(usuario);
            }
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar os membros da equipe. " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }
        return members;
    }
}