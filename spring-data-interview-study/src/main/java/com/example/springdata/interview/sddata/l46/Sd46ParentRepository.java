package com.example.springdata.interview.sddata.l46;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Sd46ParentRepository extends JpaRepository<Sd46Parent, Long> {

    List<Sd46Parent> findAll();

    @EntityGraph(attributePaths = "children")
    List<Sd46Parent> findAllByNameStartingWith(String prefix);
}
