package com.mycompany.gestaodeprojetosv2;

import com.mycompany.gestaodeprojetosv2.controller.TeamDAO;
import com.mycompany.gestaodeprojetosv2.controller.UsuarioDAO;
import com.mycompany.gestaodeprojetosv2.model.Team;
import com.mycompany.gestaodeprojetosv2.model.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDialog extends JDialog {

    private JTextField nameField;
    private JTextArea descriptionArea;
    private JList<String> membersList;
    private DefaultListModel<String> membersListModel;

    public TeamDialog(JFrame parent) {
        super(parent, "Adicionar Equipe", true);
        initComponents();
        this.setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(400, 450);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Nome da Equipe
        formPanel.add(new JLabel("Nome da Equipe:"));
        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));
        formPanel.add(nameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Descrição
        formPanel.add(new JLabel("Descrição:"));
        descriptionArea = new JTextArea(5, 20);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(descriptionScrollPane);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Membros
        formPanel.add(new JLabel("Membros:"));
        membersListModel = new DefaultListModel<>();
        membersList = new JList<>(membersListModel);
        membersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        membersList.setVisibleRowCount(8); // Aumenta o número de linhas visíveis
        JScrollPane membersScrollPane = new JScrollPane(membersList);
        
        // Aumenta o tamanho da caixinha de seleção
        membersScrollPane.setPreferredSize(new Dimension(300, 150));
        membersScrollPane.setMinimumSize(new Dimension(300, 150));
        membersScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        formPanel.add(membersScrollPane);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> saveTeam());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadMembers();
    }

    private void loadMembers() {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> usuarios = usuarioDAO.getAll();
            membersListModel.clear();
            for (Usuario u : usuarios) {
                membersListModel.addElement(u.getNomeCompleto() + " - " + u.getEmail());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveTeam() {
        String name = nameField.getText();
        String description = descriptionArea.getText();
        List<String> selectedMembers = membersList.getSelectedValuesList();

        if (name.isEmpty() || description.isEmpty() || selectedMembers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos e selecione os membros.", "Campos Faltando", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Team team = new Team();
        team.setName(name);
        team.setDescription(description);

        try {
            // Obter os objetos Usuario dos membros selecionados
            List<Usuario> members = new ArrayList<>();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> allUsers = usuarioDAO.getAll();

            for (String selectedMember : selectedMembers) {
                String email = selectedMember.substring(selectedMember.lastIndexOf("- ") + 2);
                for (Usuario user : allUsers) {
                    if (user.getEmail().equals(email)) {
                        members.add(user);
                        break;
                    }
                }
            }
            team.setMembers(members);

            TeamDAO teamDAO = new TeamDAO();
            teamDAO.save(team);
            JOptionPane.showMessageDialog(this, "Equipe salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar a equipe: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}