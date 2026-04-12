package com.example.jpa.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "study_invoices")
public class StudyInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String number;

    protected StudyInvoice() {
    }

    public StudyInvoice(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }
}
