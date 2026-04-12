package com.example.jpa.interview.entity;

import com.example.jpa.interview.converter.TrimConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Convert(converter = TrimConverter.class)
    @Column(name = "sku", length = 64)
    private String sku;

    protected Product() {
    }

    public Product(String name, String sku) {
        this.name = name;
        this.sku = sku;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }
}
