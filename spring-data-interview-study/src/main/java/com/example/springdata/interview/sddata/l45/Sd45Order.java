package com.example.springdata.interview.sddata.l45;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
@Table(name = "sd45_order")
public class Sd45Order extends AbstractAggregateRoot<Sd45Order> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String state;

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    /** Call after the aggregate has an id (first {@code save}) so the event carries a real key. */
    public void markPlaced() {
        registerEvent(new Sd45PlacedEvent(id));
    }
}
