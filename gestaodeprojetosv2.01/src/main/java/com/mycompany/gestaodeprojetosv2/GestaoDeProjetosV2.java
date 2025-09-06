package com.mycompany.gestaodeprojetosv2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestaoDeProjetosV2 {

    // --- Configurações do Banco de Dados ---
    // Conexão com o MySQL.
    // Nosso banco de dados está com o nome "projects".
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/projects?useTimezone=true&serverTimezone=UTC";
    // Nome de usuário para acessar o banco de dados.
    private static final String DATABASE_USER = "root";
    // Senha do usuário.
    private static final String DATABASE_PASSWORD = "58919811Ana!";

    // --- Método de Conexão ---
    
    public static Connection getConnection() throws SQLException {
        try {
            // Tenta estabelecer a conexão usando a URL, o usuário e a senha.
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (SQLException e) {
            // Em caso de erro na conexão.
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }
}