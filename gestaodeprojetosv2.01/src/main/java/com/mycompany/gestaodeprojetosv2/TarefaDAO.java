package com.mycompany.gestaodeprojetosv2.controller;

import com.mycompany.gestaodeprojetosv2.model.Tarefa;
import com.mycompany.gestaodeprojetosv2.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    public void save(Tarefa tarefa) {
        String sql = "INSERT INTO tasks (title, description, projectId, assignee, status, startDate, endDate, realStartDate, realEndDate, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tarefa.getTitle());
            stmt.setString(2, tarefa.getDescription());
            stmt.setInt(3, tarefa.getProjectId());
            stmt.setString(4, tarefa.getAssignee());
            stmt.setString(5, tarefa.getStatus());
            stmt.setDate(6, new Date(tarefa.getStartDate().getTime()));
            stmt.setDate(7, new Date(tarefa.getEndDate().getTime()));
            stmt.setDate(8, tarefa.getRealStartDate() != null ? new Date(tarefa.getRealStartDate().getTime()) : null);
            stmt.setDate(9, tarefa.getRealEndDate() != null ? new Date(tarefa.getRealEndDate().getTime()) : null);
            stmt.setDate(10, new Date(tarefa.getCreatedAt().getTime()));
            stmt.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao salvar a tarefa. " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }
    }

    public List<Tarefa> getByProjectId(int projectId) {
        String sql = "SELECT * FROM tasks WHERE projectId = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Tarefa> tarefas = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, projectId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(rs.getInt("id"));
                tarefa.setTitle(rs.getString("title"));
                tarefa.setDescription(rs.getString("description"));
                tarefa.setProjectId(rs.getInt("projectId"));
                tarefa.setAssignee(rs.getString("assignee"));
                tarefa.setStatus(rs.getString("status"));
                tarefa.setStartDate(rs.getDate("startDate"));
                tarefa.setEndDate(rs.getDate("endDate"));
                tarefa.setRealStartDate(rs.getDate("realStartDate"));
                tarefa.setRealEndDate(rs.getDate("realEndDate"));
                tarefa.setCreatedAt(rs.getDate("createdAt"));
                tarefas.add(tarefa);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar tarefas por projeto. " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }
        return tarefas;
    }
}