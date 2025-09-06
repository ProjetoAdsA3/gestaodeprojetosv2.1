package com.mycompany.gestaodeprojetosv2;

import com.mycompany.gestaodeprojetosv2.controller.ProjetoDAO;
import com.mycompany.gestaodeprojetosv2.controller.TarefaDAO;
import com.mycompany.gestaodeprojetosv2.controller.UsuarioDAO;
import com.mycompany.gestaodeprojetosv2.controller.TeamDAO;
import com.mycompany.gestaodeprojetosv2.model.Projeto;
import com.mycompany.gestaodeprojetosv2.model.Tarefa;
import com.mycompany.gestaodeprojetosv2.model.Usuario;
import com.mycompany.gestaodeprojetosv2.model.Team;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainScreen extends JFrame {

    private JPanel mainPanel;
    private JPanel sideMenuPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    private JTable projectsTable;
    private JTable tasksTable;
    private JTable usersTable;
    private JTable teamsTable;

    public MainScreen() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gerenciador de Projetos");
        setSize(1024, 768);

        mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        sideMenuPanel = new JPanel();
        sideMenuPanel.setBackground(new Color(40, 44, 52));
        sideMenuPanel.setPreferredSize(new Dimension(220, getHeight()));
        sideMenuPanel.setLayout(new BoxLayout(sideMenuPanel, BoxLayout.Y_AXIS));
        mainPanel.add(sideMenuPanel, BorderLayout.WEST);

        JLabel logoLabel = new JLabel("Gestão de Projetos", SwingConstants.CENTER);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        sideMenuPanel.add(logoLabel);

        addMenuItem("Projetos", "projectsPanel");
        addMenuItem("Tarefas", "tasksPanel");
        addMenuItem("Usuários", "usersPanel");
        addMenuItem("Equipes", "teamsPanel");
        addMenuItem("Relatórios", "reportsPanel");
        addMenuItem("Dashboards", "dashboardPanel");

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(createProjectsSectionPanel(), "projectsPanel");
        contentPanel.add(createTasksSectionPanel(), "tasksPanel");
        contentPanel.add(createUsersSectionPanel(), "usersPanel");
        contentPanel.add(createTeamsSectionPanel(), "teamsPanel");
        contentPanel.add(createReportsSectionPanel(), "reportsPanel");
        contentPanel.add(createDashboardSectionPanel(), "dashboardPanel");

        cardLayout.show(contentPanel, "projectsPanel");
    }

    private void addMenuItem(String text, String cardName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setBackground(new Color(50, 54, 62));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            cardLayout.show(contentPanel, cardName);
            if (cardName.equals("projectsPanel")) {
                loadProjectsTable((DefaultTableModel) projectsTable.getModel());
            } else if (cardName.equals("tasksPanel")) {
            } else if (cardName.equals("usersPanel")) {
                loadUsersTable((DefaultTableModel) usersTable.getModel());
            } else if (cardName.equals("teamsPanel")) {
                loadTeamsTable((DefaultTableModel) teamsTable.getModel());
            }
        });

        sideMenuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sideMenuPanel.add(button);
    }

    private JPanel createProjectsSectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Projetos");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Adicionar Projeto");
        addButton.addActionListener(e -> {
            ProjectDialog dialog = new ProjectDialog(MainScreen.this);
            dialog.setVisible(true);
            loadProjectsTable((DefaultTableModel) projectsTable.getModel());
        });
        actionPanel.add(addButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Nome", "Descrição", "Status", "Gerente", "Data de Início", "Data de Término"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        projectsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(projectsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadProjectsTable(tableModel);

        return panel;
    }

    private void loadProjectsTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            ProjetoDAO projetoDAO = new ProjetoDAO();
            List<Projeto> projetos = projetoDAO.getAll(); 

            for (Projeto p : projetos) {
                model.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getDescricao(),
                    p.getStatus(),
                    p.getGerenteResponsavel(),
                    p.getDataInicio(),
                    p.getDataTermino()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar projetos do banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private JPanel createTasksSectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Tarefas");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Adicionar Tarefa");
        addButton.addActionListener(e -> {
            TaskDialog dialog = new TaskDialog(MainScreen.this);
            dialog.setVisible(true);
        });
        actionPanel.add(addButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Título", "Descrição", "Projeto ID", "Responsável", "Status", "Início Previsto", "Fim Previsto"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        tasksTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tasksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadTasksTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            TarefaDAO tarefaDAO = new TarefaDAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar tarefas do banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JPanel createUsersSectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Usuários");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Adicionar Usuário");
        addButton.addActionListener(e -> {
            UserDialog dialog = new UserDialog(MainScreen.this);
            dialog.setVisible(true);
            loadUsersTable((DefaultTableModel) usersTable.getModel());
        });
        actionPanel.add(addButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Nome Completo", "CPF", "E-mail", "Cargo", "Login", "Perfil"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        usersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(usersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadUsersTable(tableModel);

        return panel;
    }

    private void loadUsersTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> usuarios = usuarioDAO.getAll();

            for (Usuario u : usuarios) {
                model.addRow(new Object[]{
                    u.getId(),
                    u.getNomeCompleto(),
                    u.getCpf(),
                    u.getEmail(),
                    u.getCargo(),
                    u.getLogin(),
                    u.getPerfil()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários do banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JPanel createTeamsSectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Equipes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Adicionar Equipe");
        addButton.addActionListener(e -> {
            TeamDialog dialog = new TeamDialog(MainScreen.this);
            dialog.setVisible(true);
            loadTeamsTable((DefaultTableModel) teamsTable.getModel());
        });
        actionPanel.add(addButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Nome da Equipe", "Descrição", "Membros"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        teamsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(teamsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadTeamsTable(tableModel);

        return panel;
    }
    
    private void loadTeamsTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            TeamDAO teamDAO = new TeamDAO();
            List<Team> teams = teamDAO.getAll();
            
            for (Team team : teams) {
                String membersString = "";
                if (team.getMembers() != null && !team.getMembers().isEmpty()) {
                    for (Usuario member : team.getMembers()) {
                        membersString += member.getNomeCompleto() + "; ";
                    }
                    membersString = membersString.substring(0, membersString.length() - 2); // Remove o último "; "
                }
                
                model.addRow(new Object[]{
                    team.getId(),
                    team.getName(),
                    team.getDescription(),
                    membersString
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar equipes do banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JPanel createReportsSectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("Relatórios");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createDashboardSectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainScreen().setVisible(true);
        });
    }
}