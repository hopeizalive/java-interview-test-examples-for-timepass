package com.example.jpa.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_lines")
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected OrderLine() {
    }

    public OrderLine(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }
}
