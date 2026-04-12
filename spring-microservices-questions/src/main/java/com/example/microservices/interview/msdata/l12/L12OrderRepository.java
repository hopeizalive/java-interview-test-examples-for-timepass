package com.example.microservices.interview.msdata.l12;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface L12OrderRepository extends JpaRepository<L12Order, Long> {

    @Query("select o from L12Order o where o.id = :id")
    Optional<L12Order> loadOrderWithoutLines(@Param("id") Long id);

    @Override
    @EntityGraph(attributePaths = {"lines"})
    Optional<L12Order> findById(Long id);
}
