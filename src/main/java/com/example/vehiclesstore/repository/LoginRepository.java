package com.example.vehiclesstore.repository;

import com.example.vehiclesstore.model.Login;
import org.springframework.data.repository.CrudRepository;

public interface LoginRepository extends CrudRepository<Login, Integer> {
    Login findByEmail(String email);
}
