package com.example.jpa.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;

    @Column(nullable = false, length = 200)
    private String name;

    protected Job() {
    }

    public Job(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }
}
