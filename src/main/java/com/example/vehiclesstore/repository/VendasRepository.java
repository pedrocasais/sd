package com.example.vehiclesstore.repository;


import com.example.vehiclesstore.model.Estatisticas;
import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.model.Vendas;
import com.example.vehiclesstore.model.VendasMensal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface VendasRepository extends CrudRepository<Vendas, Integer> {

    @Query("SELECT new com.example.vehiclesstore.model.Estatisticas(v.user.nome, COUNT(v)) " +
            "FROM Vendas v GROUP BY v.user.nome ORDER BY COUNT(v) DESC")
    ArrayList<Estatisticas> findTopClientes();

    List<Vendas> findByUser(Users user);

    /*@Query("SELECT new com.example.vehiclesstore.model.VendasMensal(MONTH(v.dataVenda), SUM(v.precoVenda)) " +
            "FROM Vendas v " +
            "GROUP BY MONTH(v.dataVenda) " +
            "ORDER BY MONTH(v.dataVenda)")
    List<VendasMensal> findTotalVendasPorMes();


     */
}