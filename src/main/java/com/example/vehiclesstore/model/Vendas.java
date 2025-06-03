package com.example.vehiclesstore.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class Vendas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idVenda;

    private LocalDateTime dataVenda;
    private double precoVenda;
    private long refPagamento;
    private int nif;

    @ManyToOne
    @JoinColumn(name = "veiculo_id") // Chave estrangeira Veiculos
    private Veiculos veiculo;

    @ManyToOne
    @JoinColumn(name = "user_id") // Chave estrangeira Users
    private Users user;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Veiculos getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculos veiculo) {
        this.veiculo = veiculo;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public long getRefPagamento() {
        return refPagamento;
    }

    public void setRefPagamento(Long refPagamento) {
        this.refPagamento = refPagamento;
    }

    public int getNif() {
        return nif;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }


}