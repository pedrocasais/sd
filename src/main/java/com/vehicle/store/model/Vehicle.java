package com.vehicle.store.model;

import jakarta.persistence.*;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private int year;
    private double price;

    @ManyToOne
    private Category category;

    private int stockQuantity;

    // getters e setters
}
