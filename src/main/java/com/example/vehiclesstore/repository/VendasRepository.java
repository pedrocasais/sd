package com.example.vehiclesstore.repository;

import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.model.Vendas;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VendasRepository extends CrudRepository<Vendas, Integer> {

    List<Vendas> findByUser(Users user);

}