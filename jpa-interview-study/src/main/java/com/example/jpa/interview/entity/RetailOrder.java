package com.example.jpa.interview.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "retail_orders")
public class RetailOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private OrderStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "legacy_created")
    private java.util.Date legacyCreated;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected RetailOrder() {
    }

    public RetailOrder(OrderStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public java.util.Date getLegacyCreated() {
        return legacyCreated;
    }

    public void setLegacyCreated(java.util.Date legacyCreated) {
        this.legacyCreated = legacyCreated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
