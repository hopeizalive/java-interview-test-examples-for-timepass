package com.example.jpa.interview.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("WIRE")
public class WirePayment extends Payment {

    @Column(name = "iban", length = 34)
    private String iban;

    protected WirePayment() {
    }

    public WirePayment(BigDecimal amount, String iban) {
        super(amount);
        this.iban = iban;
    }

    public String getIban() {
        return iban;
    }
}
