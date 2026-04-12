package com.example.jpa.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, length = 20)
    private String seat;

    protected Reservation() {
    }

    public Reservation(String seat) {
        this.seat = seat;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public String getSeat() {
        return seat;
    }
}
