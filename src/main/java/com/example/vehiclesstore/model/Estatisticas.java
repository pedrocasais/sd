package com.example.vehiclesstore.model;

public class Estatisticas {

    private String nome;
    private Long totalCompras;

    public Estatisticas(String nome, Long totalCompras) {
        this.nome = nome;
        this.totalCompras = totalCompras;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public Long getTotalCompras() {
        return totalCompras;
    }

}
