package com.mycompany.gestaodeprojetosv2.model;

import java.util.List;

public class Team {

    private int id;
    private String name;
    private String description;
    private List<Usuario> members; // Relacionamento com a classe Usuario

    public Team() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Usuario> getMembers() {
        return members;
    }

    public void setMembers(List<Usuario> members) {
        this.members = members;
    }
}