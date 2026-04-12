package com.example.jpa.interview.entity;

import com.example.jpa.interview.listener.AuditingListener;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@EntityListeners(AuditingListener.class)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    private LocalDateTime createdAt;

    protected Article() {
    }

    public Article(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
