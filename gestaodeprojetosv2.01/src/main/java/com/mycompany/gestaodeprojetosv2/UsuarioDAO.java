package com.mycompany.gestaodeprojetosv2.controller;

import com.mycompany.gestaodeprojetosv2.model.Usuario;
import com.mycompany.gestaodeprojetosv2.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void save(Usuario usuario) {
        String sql = "INSERT INTO users (fullName, cpf, email, role, login, password, profile) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getCargo());
            stmt.setString(5, usuario.getLogin());
            stmt.setString(6, usuario.getSenha());
            stmt.setString(7, usuario.getPerfil());
            stmt.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao salvar o usuário. " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }
    }

    public List<Usuario> getAll() {
        String sql = "SELECT * FROM users";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNomeCompleto(rs.getString("fullName"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCargo(rs.getString("role"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("password"));
                usuario.setPerfil(rs.getString("profile"));
                usuarios.add(usuario);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar os usuários. " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }
        return usuarios;
    }

    public boolean validateLogin(String login, String senha) {
        String sql = "SELECT * FROM users WHERE login = ? AND password = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();

            return rs.next(); // Retorna true se encontrar um usuário, false caso contrário
        } catch (SQLException ex) {
            System.err.println("Erro ao validar login: " + ex.getMessage());
            return false;
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }
    }
}