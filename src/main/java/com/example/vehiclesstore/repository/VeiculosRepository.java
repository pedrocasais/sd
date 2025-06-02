package com.example.vehiclesstore.repository;
import com.example.vehiclesstore.model.Veiculos;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface VeiculosRepository extends CrudRepository<Veiculos, Integer>{
    Veiculos findByID(int id);


    List<Veiculos> findByMarcaAndAno(String marca, String ano);

    List<Veiculos> findByMarca(String marca);

    List<Veiculos> findByAno(String ano);

    List<Veiculos> findAll();

    @Query("SELECT DISTINCT v.marca FROM Veiculos v ORDER BY v.marca")
    List<String> listarMarcas();

    @Query("SELECT DISTINCT v.ano FROM Veiculos v ORDER BY v.ano DESC")
    List<String> listarAnos();

    List<Veiculos> findByPrecoLessThanEqual(Double preco);

    List<Veiculos> findByMarcaAndPrecoLessThanEqual(String marca, Double preco);

    List<Veiculos> findByAnoAndPrecoLessThanEqual(String ano, Double preco);

    List<Veiculos> findByMarcaAndAnoAndPrecoLessThanEqual(String marca, String ano, Double preco);

    List<Veiculos> findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(String marca, String modelo);

    List<Veiculos> findByEstadoNot(String estado);

    // CONTADOR DE CARROS VENDIDOS
    //Long countByEstado(String estado);

}


