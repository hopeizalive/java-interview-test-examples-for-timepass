package com.example.jpa.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zip", column = @Column(name = "postal_code"))
    })
    private Address homeAddress;

    protected Customer() {
    }

    public Customer(String name, Address homeAddress) {
        this.name = name;
        this.homeAddress = homeAddress;
    }

    public Long getId() {
        return id;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }
}
