package com.mycompany.gestaodeprojetosv2.model;

import java.util.List;
import java.util.Date;

public class Projeto {

    private int id;
    private String nome;
    private String descricao;
    private String status;
    private String gerenteResponsavel;
    private String dataInicio;
    private String dataTermino;
    private List<Team> equipes;

    public Projeto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGerenteResponsavel() {
        return gerenteResponsavel;
    }

    public void setGerenteResponsavel(String gerenteResponsavel) {
        this.gerenteResponsavel = gerenteResponsavel;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(String dataTermino) {
        this.dataTermino = dataTermino;
    }

    public List<Team> getEquipes() {
        return equipes;
    }

    public void setEquipes(List<Team> equipes) {
        this.equipes = equipes;
    }
}