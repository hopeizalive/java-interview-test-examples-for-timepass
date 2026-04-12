package com.example.jpa.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(fetch = FetchType.EAGER)
    @Column(nullable = false, length = 2)
    private String code;

    protected Country() {
    }

    public Country(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}
