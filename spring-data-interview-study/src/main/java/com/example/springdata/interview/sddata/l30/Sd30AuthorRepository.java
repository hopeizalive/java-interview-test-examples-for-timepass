package com.example.springdata.interview.sddata.l30;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Sd30AuthorRepository extends JpaRepository<Sd30Author, Long> {

    /** Join fetch multiplies parent rows (one per child) unless you dedupe or use distinct + careful countQuery. */
    @Query("select a from Sd30Author a join fetch a.books")
    List<Sd30Author> findAllJoinBooks();
}
