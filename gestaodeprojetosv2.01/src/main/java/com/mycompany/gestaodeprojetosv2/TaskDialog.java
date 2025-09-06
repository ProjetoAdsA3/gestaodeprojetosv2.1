package com.mycompany.gestaodeprojetosv2;

import com.mycompany.gestaodeprojetosv2.controller.ProjetoDAO;
import com.mycompany.gestaodeprojetosv2.controller.TarefaDAO;
import com.mycompany.gestaodeprojetosv2.model.Projeto;
import com.mycompany.gestaodeprojetosv2.model.Tarefa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDialog extends JDialog {

    private JLabel labelTitle;
    private JTextField textFieldTitle;
    private JLabel labelDescription;
    private JTextArea textAreaDescription;
    private JLabel labelProjectId;
    private JComboBox<String> comboBoxProjects;
    private JLabel labelAssignee;
    private JTextField textFieldAssignee;
    private JLabel labelStatus;
    private JComboBox<String> comboBoxStatus;
    private JLabel labelStartDate;
    private JTextField textFieldStartDate;
    private JLabel labelEndDate;
    private JTextField textFieldEndDate;
    private JLabel labelRealStartDate;
    private JTextField textFieldRealStartDate;
    private JLabel labelRealEndDate;
    private JTextField textFieldRealEndDate;

    private JButton buttonSalvar;
    private JButton buttonCancelar;

    private TarefaDAO tarefaDAO;
    private ProjetoDAO projetoDAO;
    private Map<String, Integer> projectMap;

    public TaskDialog(Frame owner) {
        super(owner, "Adicionar Tarefa", true);
        this.tarefaDAO = new TarefaDAO();
        this.projetoDAO = new ProjetoDAO();
        this.projectMap = new HashMap<>();
        initComponents();
        loadProjects();
        this.setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setSize(400, 600);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        labelTitle = new JLabel("Título:");
        add(labelTitle, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        textFieldTitle = new JTextField(20);
        add(textFieldTitle, gbc);

        // Descrição
        gbc.gridx = 0; gbc.gridy = 1;
        labelDescription = new JLabel("Descrição:");
        add(labelDescription, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        textAreaDescription = new JTextArea(5, 20);
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(textAreaDescription);
        add(scrollDescricao, gbc);

        // Projeto Vinculado
        gbc.gridx = 0; gbc.gridy = 2;
        labelProjectId = new JLabel("Projeto:");
        add(labelProjectId, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        comboBoxProjects = new JComboBox<>();
        add(comboBoxProjects, gbc);

        // Responsável (Usuário)
        gbc.gridx = 0; gbc.gridy = 3;
        labelAssignee = new JLabel("Responsável:");
        add(labelAssignee, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        textFieldAssignee = new JTextField(20);
        add(textFieldAssignee, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 4;
        labelStatus = new JLabel("Status:");
        add(labelStatus, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        String[] statusOptions = {"pendente", "em execução", "concluída"};
        comboBoxStatus = new JComboBox<>(statusOptions);
        add(comboBoxStatus, gbc);

        // Data de Início Prevista
        gbc.gridx = 0; gbc.gridy = 5;
        labelStartDate = new JLabel("Início Previsto (dd/MM/yyyy):");
        add(labelStartDate, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        textFieldStartDate = new JTextField(20);
        add(textFieldStartDate, gbc);
        
        // Data de Fim Prevista
        gbc.gridx = 0; gbc.gridy = 6;
        labelEndDate = new JLabel("Fim Previsto (dd/MM/yyyy):");
        add(labelEndDate, gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        textFieldEndDate = new JTextField(20);
        add(textFieldEndDate, gbc);

        // Data de Início Real
        gbc.gridx = 0; gbc.gridy = 7;
        labelRealStartDate = new JLabel("Início Real (dd/MM/yyyy):");
        add(labelRealStartDate, gbc);
        gbc.gridx = 1; gbc.gridy = 7;
        textFieldRealStartDate = new JTextField(20);
        add(textFieldRealStartDate, gbc);
        
        // Data de Fim Real
        gbc.gridx = 0; gbc.gridy = 8;
        labelRealEndDate = new JLabel("Fim Real (dd/MM/yyyy):");
        add(labelRealEndDate, gbc);
        gbc.gridx = 1; gbc.gridy = 8;
        textFieldRealEndDate = new JTextField(20);
        add(textFieldRealEndDate, gbc);
        
        // Botões
        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 1;
        buttonSalvar = new JButton("Salvar");
        add(buttonSalvar, gbc);

        gbc.gridx = 1; gbc.gridy = 9;
        buttonCancelar = new JButton("Cancelar");
        add(buttonCancelar, gbc);

        // Ações dos botões
        buttonSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarTarefa();
            }
        });

        buttonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void loadProjects() {
        try {
            List<Projeto> projetos = projetoDAO.getAll();
            for (Projeto p : projetos) {
                comboBoxProjects.addItem(p.getNome());
                projectMap.put(p.getNome(), p.getId());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar projetos para seleção: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarTarefa() {
        try {
            Tarefa tarefa = new Tarefa();
            tarefa.setTitle(textFieldTitle.getText());
            tarefa.setDescription(textAreaDescription.getText());
            
            String selectedProjectName = (String) comboBoxProjects.getSelectedItem();
            if (selectedProjectName != null && projectMap.containsKey(selectedProjectName)) {
                tarefa.setProjectId(projectMap.get(selectedProjectName));
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um projeto válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tarefa.setAssignee(textFieldAssignee.getText());
            tarefa.setStatus(comboBoxStatus.getSelectedItem().toString());
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tarefa.setStartDate(dateFormat.parse(textFieldStartDate.getText()));
            tarefa.setEndDate(dateFormat.parse(textFieldEndDate.getText()));
            
            String realStartDateText = textFieldRealStartDate.getText();
            if (!realStartDateText.isEmpty()) {
                tarefa.setRealStartDate(dateFormat.parse(realStartDateText));
            }

            String realEndDateText = textFieldRealEndDate.getText();
            if (!realEndDateText.isEmpty()) {
                tarefa.setRealEndDate(dateFormat.parse(realEndDateText));
            }

            tarefaDAO.save(tarefa);
            JOptionPane.showMessageDialog(this, "Tarefa salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Erro de formato de data. Use o formato dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar tarefa: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}