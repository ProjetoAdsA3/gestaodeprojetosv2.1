package com.mycompany.gestaodeprojetosv2.service;

import com.mycompany.gestaodeprojetosv2.controller.UsuarioDAO;

public class AuthService {

    private UsuarioDAO usuarioDAO;

    public AuthService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean authenticate(String username, String password) {
        return usuarioDAO.validateLogin(username, password);
    }
}