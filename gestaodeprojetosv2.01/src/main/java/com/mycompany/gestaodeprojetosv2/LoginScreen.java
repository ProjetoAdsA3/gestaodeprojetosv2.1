package com.mycompany.gestaodeprojetosv2;

import com.mycompany.gestaodeprojetosv2.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginScreen() {
        initComponents();
        this.setLocationRelativeTo(null); 
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login - Gerenciador de Projetos");
        setSize(400, 300);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10)); 
        panel.setBorder(new EmptyBorder(20, 20, 20, 20)); 
        
        JLabel titleLabel = new JLabel("Acessar o Sistema", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(titleLabel);

        JPanel userPanel = new JPanel(new BorderLayout(5, 0));
        userPanel.add(new JLabel("Usuário:"), BorderLayout.WEST);
        usernameField = new JTextField();
        userPanel.add(usernameField, BorderLayout.CENTER);
        panel.add(userPanel);

        JPanel passPanel = new JPanel(new BorderLayout(5, 0));
        passPanel.add(new JLabel("Senha:"), BorderLayout.WEST);
        passwordField = new JPasswordField();
        passPanel.add(passwordField, BorderLayout.CENTER);
        panel.add(passPanel);

        loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
        panel.add(loginButton);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        AuthService authService = new AuthService();
        if (authService.authenticate(username, password)) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            MainScreen mainScreen = new MainScreen();
            mainScreen.setVisible(true);
            this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
}