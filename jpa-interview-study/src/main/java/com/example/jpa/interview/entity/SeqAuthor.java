package com.example.jpa.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seq_authors")
@SequenceGenerator(
        name = "seq_author_gen",
        sequenceName = "author_sequence",
        allocationSize = 1
)
public class SeqAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_author_gen")
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    protected SeqAuthor() {
    }

    public SeqAuthor(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }
}
