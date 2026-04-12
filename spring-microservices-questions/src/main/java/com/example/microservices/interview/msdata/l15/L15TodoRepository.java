package com.example.microservices.interview.msdata.l15;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface L15TodoRepository extends JpaRepository<L15Todo, Long> {

    List<L15Todo> findByDoneFalse();

    @Query("select t from L15Todo t where lower(t.title) like lower(concat('%', :q, '%'))")
    List<L15Todo> searchTitle(String q);
}
