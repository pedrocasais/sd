package com.example.vehiclesstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Users{

    @Id //depId is the primary key
    private Integer ID;
    private String role;
    private String nome;
    private String email;

    // TODO: nao sei quais sao os outros fields

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


