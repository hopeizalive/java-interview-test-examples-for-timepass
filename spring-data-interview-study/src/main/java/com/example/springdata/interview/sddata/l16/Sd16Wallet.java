package com.example.springdata.interview.sddata.l16;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sd16_wallet")
public class Sd16Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int balanceCents;

    public Long getId() {
        return id;
    }

    public int getBalanceCents() {
        return balanceCents;
    }

    public void setBalanceCents(int balanceCents) {
        this.balanceCents = balanceCents;
    }
}
