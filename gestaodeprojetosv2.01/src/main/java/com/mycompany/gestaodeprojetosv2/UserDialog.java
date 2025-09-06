package com.mycompany.gestaodeprojetosv2;

import com.mycompany.gestaodeprojetosv2.controller.UsuarioDAO;
import com.mycompany.gestaodeprojetosv2.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDialog extends JDialog {

    private JLabel labelNomeCompleto;
    private JTextField textFieldNomeCompleto;
    private JLabel labelCpf;
    private JFormattedTextField formattedTextFieldCpf;
    private JLabel labelEmail;
    private JTextField textFieldEmail;
    private JLabel labelCargo;
    private JTextField textFieldCargo;
    private JLabel labelLogin;
    private JTextField textFieldLogin;
    private JLabel labelSenha;
    private JPasswordField passwordFieldSenha;
    private JLabel labelPerfil;
    private JComboBox<String> comboBoxPerfil;

    private JButton buttonSalvar;
    private JButton buttonCancelar;

    private UsuarioDAO usuarioDAO;

    public UserDialog(Frame owner) {
        super(owner, "Adicionar Usuário", true);
        this.usuarioDAO = new UsuarioDAO();
        initComponents();
        this.setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setSize(400, 550);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome Completo
        gbc.gridx = 0; gbc.gridy = 0;
        labelNomeCompleto = new JLabel("Nome Completo:");
        add(labelNomeCompleto, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        textFieldNomeCompleto = new JTextField(20);
        add(textFieldNomeCompleto, gbc);

        // CPF
        gbc.gridx = 0; gbc.gridy = 1;
        labelCpf = new JLabel("CPF:");
        add(labelCpf, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        try {
            formattedTextFieldCpf = new JFormattedTextField(new javax.swing.text.MaskFormatter("###.###.###-##"));
            formattedTextFieldCpf.setColumns(20);
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
            formattedTextFieldCpf = new JFormattedTextField();
        }
        add(formattedTextFieldCpf, gbc);

        // E-mail
        gbc.gridx = 0; gbc.gridy = 2;
        labelEmail = new JLabel("E-mail:");
        add(labelEmail, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        textFieldEmail = new JTextField(20);
        add(textFieldEmail, gbc);

        // Cargo
        gbc.gridx = 0; gbc.gridy = 3;
        labelCargo = new JLabel("Cargo:");
        add(labelCargo, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        textFieldCargo = new JTextField(20);
        add(textFieldCargo, gbc);

        // Login
        gbc.gridx = 0; gbc.gridy = 4;
        labelLogin = new JLabel("Login:");
        add(labelLogin, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        textFieldLogin = new JTextField(20);
        add(textFieldLogin, gbc);

        // Senha
        gbc.gridx = 0; gbc.gridy = 5;
        labelSenha = new JLabel("Senha:");
        add(labelSenha, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        passwordFieldSenha = new JPasswordField(20);
        add(passwordFieldSenha, gbc);

        // Perfil
        gbc.gridx = 0; gbc.gridy = 6;
        labelPerfil = new JLabel("Perfil:");
        add(labelPerfil, gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        String[] perfilOptions = {"administrador", "gerente", "colaborador"};
        comboBoxPerfil = new JComboBox<>(perfilOptions);
        add(comboBoxPerfil, gbc);

        // Botões
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 1;
        buttonSalvar = new JButton("Salvar");
        add(buttonSalvar, gbc);

        gbc.gridx = 1; gbc.gridy = 7;
        buttonCancelar = new JButton("Cancelar");
        add(buttonCancelar, gbc);

        // Ações dos botões
        buttonSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarUsuario();
            }
        });

        buttonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void salvarUsuario() {
        try {
            String nomeCompleto = textFieldNomeCompleto.getText();
            String cpf = formattedTextFieldCpf.getText().replace(".", "").replace("-", ""); // Remover pontos e traços do CPF
            String email = textFieldEmail.getText();
            String cargo = textFieldCargo.getText();
            String login = textFieldLogin.getText();
            String senha = new String(passwordFieldSenha.getPassword());
            String perfil = (String) comboBoxPerfil.getSelectedItem();

            if (nomeCompleto.isEmpty() || cpf.isEmpty() || email.isEmpty() || login.isEmpty() || senha.isEmpty() || perfil.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos obrigatórios devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um e-mail válido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNomeCompleto(nomeCompleto);
            usuario.setCpf(cpf);
            usuario.setEmail(email);
            usuario.setCargo(cargo);
            usuario.setLogin(login);
            usuario.setSenha(senha); 
            usuario.setPerfil(perfil);

            usuarioDAO.save(usuario);
            JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    // Método para validação de e-mail
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}