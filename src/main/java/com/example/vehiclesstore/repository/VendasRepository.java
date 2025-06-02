package com.example.vehiclesstore.repository;

import com.example.vehiclesstore.model.Estatisticas;
import com.example.vehiclesstore.model.Vendas;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface VendasRepository extends CrudRepository<Vendas, Integer> {

    @Query("SELECT new com.example.vehiclesstore.model.Estatisticas(v.user.nome, COUNT(v)) " +
            "FROM Vendas v GROUP BY v.user.nome ORDER BY COUNT(v) DESC")
    ArrayList<Estatisticas> findTopClientes();

}