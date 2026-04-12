package com.example.microservices.interview.msdata.l13;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface L13ProductRepository extends JpaRepository<L13Product, Long> {

    Page<L13Product> findAllByOrderByIdAsc(Pageable pageable);
}
