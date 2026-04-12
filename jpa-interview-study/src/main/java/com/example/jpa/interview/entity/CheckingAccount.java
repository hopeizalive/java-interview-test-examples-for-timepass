package com.example.jpa.interview.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "checking_accounts")
@PrimaryKeyJoinColumn(name = "account_id")
public class CheckingAccount extends Account {

    @Column(name = "overdraft", precision = 19, scale = 2)
    private BigDecimal overdraft;

    protected CheckingAccount() {
    }

    public CheckingAccount(String owner, BigDecimal overdraft) {
        super(owner);
        this.overdraft = overdraft;
    }

    public BigDecimal getOverdraft() {
        return overdraft;
    }
}
