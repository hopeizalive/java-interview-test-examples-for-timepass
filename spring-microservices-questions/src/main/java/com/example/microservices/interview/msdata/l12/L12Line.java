package com.example.microservices.interview.msdata.l12;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class L12Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    private L12Order order;

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public L12Order getOrder() {
        return order;
    }

    public void setOrder(L12Order order) {
        this.order = order;
    }
}
