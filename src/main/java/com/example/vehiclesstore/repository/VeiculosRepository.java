package com.example.vehiclesstore.repository;
import com.example.vehiclesstore.model.Veiculos;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface VeiculosRepository extends CrudRepository<Veiculos, Integer>{


    List<Veiculos> findByMarcaAndAno(String marca, String ano);

    List<Veiculos> findByMarca(String marca);

    List<Veiculos> findByAno(String ano);

    List<Veiculos> findAll();

}


