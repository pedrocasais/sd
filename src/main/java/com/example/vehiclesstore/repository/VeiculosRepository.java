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

    List<Veiculos> findByPrecoLessThanEqual(Integer preco);

    List<Veiculos> findByMarcaAndPrecoLessThanEqual(String marca, Integer preco);

    List<Veiculos> findByAnoAndPrecoLessThanEqual(String ano, Integer preco);

    List<Veiculos> findByMarcaAndAnoAndPrecoLessThanEqual(String marca, String ano, Integer preco);

    List<Veiculos> findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(String marca, String modelo);

    List<Veiculos> findByEstadoNot(String estado);

}


