package com.example.jpa.interview.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CARD")
public class CardPayment extends Payment {

    @Column(name = "last_four", length = 4)
    private String lastFour;

    protected CardPayment() {
    }

    public CardPayment(BigDecimal amount, String lastFour) {
        super(amount);
        this.lastFour = lastFour;
    }

    public String getLastFour() {
        return lastFour;
    }
}
