package com.example.jpa.interview.listener;

import com.example.jpa.interview.entity.Invoice;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class AuditListener {

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof Invoice invoice) {
            invoice.setLastModified(LocalDateTime.now());
        }
    }
}
