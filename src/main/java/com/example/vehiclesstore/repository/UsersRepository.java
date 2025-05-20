package com.example.vehiclesstore.repository;

import com.example.vehiclesstore.model.Users;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Integer> {

}