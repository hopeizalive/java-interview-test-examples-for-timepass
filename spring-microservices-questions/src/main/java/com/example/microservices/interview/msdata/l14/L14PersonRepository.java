package com.example.microservices.interview.msdata.l14;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface L14PersonRepository extends JpaRepository<L14Person, Long> {

    List<L14NameOnly> findAllBy();
}
