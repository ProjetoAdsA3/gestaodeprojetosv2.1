package com.mycompany.gestaodeprojetosv2.controller;

import com.mycompany.gestaodeprojetosv2.model.Projeto;
import com.mycompany.gestaodeprojetosv2.model.Team;
import com.mycompany.gestaodeprojetosv2.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {

    public void save(Projeto projeto) {
        String sql = "INSERT INTO projects (name, description, status, responsible_manager, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setString(3, projeto.getStatus());
            stmt.setString(4, projeto.getGerenteResponsavel());
            stmt.setString(5, projeto.getDataInicio());
            stmt.setString(6, projeto.getDataTermino());
            stmt.execute();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                projeto.setId(rs.getInt(1));
            }

            // Salva as equipes do projeto
            if (projeto.getEquipes() != null && !projeto.getEquipes().isEmpty()) {
                saveProjectTeams(projeto);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao salvar o projeto. " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }
    }
    
    private void saveProjectTeams(Projeto projeto) throws SQLException {
        String sql = "INSERT INTO project_teams (project_id, team_id) VALUES (?, ?)";
        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for (Team team : projeto.getEquipes()) {
            stmt.setInt(1, projeto.getId());
            stmt.setInt(2, team.getId());
            stmt.addBatch();
        }
        stmt.executeBatch();
        ConnectionFactory.closeConnection(conn, stmt);
    }
    
    public List<Projeto> getAll() {
        String sql = "SELECT * FROM projects";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Projeto> projetos = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Projeto projeto = new Projeto();
                projeto.setId(rs.getInt("id"));
                projeto.setNome(rs.getString("name"));
                projeto.setDescricao(rs.getString("description"));
                projeto.setStatus(rs.getString("status"));
                projeto.setGerenteResponsavel(rs.getString("responsible_manager"));
                projeto.setDataInicio(rs.getString("start_date"));
                projeto.setDataTermino(rs.getString("end_date"));
                
                projetos.add(projeto);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar os projetos. " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }
        return projetos;
    }
}