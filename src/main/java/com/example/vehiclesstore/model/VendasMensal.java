package com.example.vehiclesstore.model;

public class VendasMensal {

    private int mes;
    private double total;

    public void vendaMensal(int mes, double total) {
        this.mes = mes;
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }
}
