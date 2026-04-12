package com.example.microservices.interview.msdata.l12;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class L12Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<L12Line> lines = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List<L12Line> getLines() {
        return lines;
    }

    public void addLine(L12Line line) {
        lines.add(line);
        line.setOrder(this);
    }
}
