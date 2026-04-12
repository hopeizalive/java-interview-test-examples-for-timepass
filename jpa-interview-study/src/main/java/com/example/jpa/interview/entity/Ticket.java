package com.example.jpa.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(length = 500)
    private String subject;

    protected Ticket() {
    }

    public Ticket(String subject) {
        this.subject = subject;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }
}
