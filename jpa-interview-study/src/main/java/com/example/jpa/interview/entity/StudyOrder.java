package com.example.jpa.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "study_orders")
public class StudyOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String externalRef;

    protected StudyOrder() {
    }

    public StudyOrder(String externalRef) {
        this.externalRef = externalRef;
    }

    public Long getId() {
        return id;
    }
}
