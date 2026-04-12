package com.example.jpa.interview.entity;

import com.example.jpa.interview.listener.AuditListener;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@EntityListeners(AuditListener.class)
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    private LocalDateTime lastModified;

    protected Invoice() {
    }

    public Invoice(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
}
