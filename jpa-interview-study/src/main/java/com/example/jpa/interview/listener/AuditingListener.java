package com.example.jpa.interview.listener;

import com.example.jpa.interview.entity.Article;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

public class AuditingListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof Article article) {
            article.setCreatedAt(LocalDateTime.now());
        }
    }
}
