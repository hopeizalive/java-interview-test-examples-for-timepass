package com.example.springdata.interview.sddata.l04;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Sd04CustomerRepository extends JpaRepository<Sd04Customer, Long> {

    List<Sd04Customer> findByLastNameIgnoreCaseAndActiveTrue(String lastName);
}
