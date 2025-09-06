package com.mycompany.gestaodeprojetosv2;

import com.mycompany.gestaodeprojetosv2.controller.ProjetoDAO;
import com.mycompany.gestaodeprojetosv2.controller.TeamDAO;
import com.mycompany.gestaodeprojetosv2.model.Projeto;
import com.mycompany.gestaodeprojetosv2.model.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProjectDialog extends JDialog {

    private JLabel labelNome;
    private JTextField textFieldNome;
    private JLabel labelDescricao;
    private JTextArea textAreaDescricao;
    private JLabel labelStatus;
    private JComboBox<String> comboBoxStatus;
    private JLabel labelGerente;
    private JTextField textFieldGerente;
    private JLabel labelDataInicio;
    private JTextField textFieldDataInicio;
    private JLabel labelDataTermino;
    private JTextField textFieldDataTermino;
    private JLabel labelEquipes;
    private JList<Team> listEquipes;
    private DefaultListModel<Team> listModelEquipes;

    private JButton buttonSalvar;
    private JButton buttonCancelar;

    private ProjetoDAO projetoDAO;
    private TeamDAO teamDAO;

    public ProjectDialog(Frame owner) {
        super(owner, "Adicionar Projeto", true);
        this.projetoDAO = new ProjetoDAO();
        this.teamDAO = new TeamDAO();
        initComponents();
        this.setLocationRelativeTo(owner);
        loadEquipes();
    }

    private void initComponents() {
        setSize(500, 600);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nome do Projeto
        gbc.gridx = 0; gbc.gridy = 0;
        labelNome = new JLabel("Nome do Projeto:");
        add(labelNome, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        textFieldNome = new JTextField(30);
        add(textFieldNome, gbc);

        // Descrição
        gbc.gridx = 0; gbc.gridy = 1;
        labelDescricao = new JLabel("Descrição:");
        add(labelDescricao, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridheight = 2;
        textAreaDescricao = new JTextArea(5, 30);
        textAreaDescricao.setLineWrap(true);
        textAreaDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(textAreaDescricao);
        add(scrollDescricao, gbc);
        gbc.gridheight = 1;

        // Status
        gbc.gridx = 0; gbc.gridy = 3;
        labelStatus = new JLabel("Status:");
        add(labelStatus, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        String[] statusOptions = {"Planejado", "Em Andamento", "Concluído", "Cancelado"};
        comboBoxStatus = new JComboBox<>(statusOptions);
        add(comboBoxStatus, gbc);
        
        // Gerente
        gbc.gridx = 0; gbc.gridy = 4;
        labelGerente = new JLabel("Gerente:");
        add(labelGerente, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        textFieldGerente = new JTextField(30);
        add(textFieldGerente, gbc);
        
        // Datas
        gbc.gridx = 0; gbc.gridy = 5;
        labelDataInicio = new JLabel("Data de Início:");
        add(labelDataInicio, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        textFieldDataInicio = new JTextField(30);
        add(textFieldDataInicio, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        labelDataTermino = new JLabel("Data de Término:");
        add(labelDataTermino, gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        textFieldDataTermino = new JTextField(30);
        add(textFieldDataTermino, gbc);

        // Equipes
        gbc.gridx = 0; gbc.gridy = 7;
        labelEquipes = new JLabel("Equipes (Ctrl+clique para selecionar):");
        add(labelEquipes, gbc);
        gbc.gridx = 1; gbc.gridy = 7;
        gbc.gridheight = 4;
        listModelEquipes = new DefaultListModel<>();
        listEquipes = new JList<>(listModelEquipes);
        listEquipes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Team) {
                    Team team = (Team) value;
                    setText(team.getName() + " (" + team.getDescription() + ")");
                }
                return this;
            }
        });
        JScrollPane scrollEquipes = new JScrollPane(listEquipes);
        add(scrollEquipes, gbc);
        gbc.gridheight = 1;

        // Botões
        gbc.gridx = 0; gbc.gridy = 11;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonSalvar = new JButton("Salvar");
        buttonCancelar = new JButton("Cancelar");
        buttonPanel.add(buttonSalvar);
        buttonPanel.add(buttonCancelar);
        add(buttonPanel, gbc);

        // Ações dos botões
        buttonSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarProjeto();
            }
        });

        buttonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void loadEquipes() {
        try {
            List<Team> teams = teamDAO.getAll();
            for (Team t : teams) {
                listModelEquipes.addElement(t);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar equipes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void salvarProjeto() {
        try {
            String nome = textFieldNome.getText();
            String descricao = textAreaDescricao.getText();
            String status = (String) comboBoxStatus.getSelectedItem();
            String gerente = textFieldGerente.getText();
            String dataInicio = textFieldDataInicio.getText();
            String dataTermino = textFieldDataTermino.getText();
            List<Team> equipesSelecionadas = listEquipes.getSelectedValuesList();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome do projeto é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Projeto projeto = new Projeto();
            projeto.setNome(nome);
            projeto.setDescricao(descricao);
            projeto.setStatus(status);
            projeto.setGerenteResponsavel(gerente);
            projeto.setDataInicio(dataInicio);
            projeto.setDataTermino(dataTermino);
            projeto.setEquipes(equipesSelecionadas);

            projetoDAO.save(projeto);
            JOptionPane.showMessageDialog(this, "Projeto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar projeto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}