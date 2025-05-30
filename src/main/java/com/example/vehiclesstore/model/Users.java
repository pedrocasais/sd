package com.example.vehiclesstore.model;

import jakarta.persistence.*;

import javax.swing.*;

@Entity
public class Users{

    @Id //depId is the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    private String role;
    private String nome;
    private String morada;
    private Long numTelemovel;
    private String password;
    private String email;

    // TODO: nao sei quais sao os outros fields


    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public Long getNumTelemovel() {
        return numTelemovel;
    }

    public void setNumTelemovel(Long numTelemovel) {
        this.numTelemovel = numTelemovel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = "ROLE_" + role;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}


