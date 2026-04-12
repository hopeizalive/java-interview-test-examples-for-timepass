package com.example.springdata.interview.sddata.l07;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Sd07LogEntryRepository extends JpaRepository<Sd07LogEntry, Long> {

    @Query("select e from Sd07LogEntry e")
    Page<Sd07LogEntry> pageAll(Pageable pageable);

    @Query("select e from Sd07LogEntry e")
    Slice<Sd07LogEntry> sliceAll(Pageable pageable);
}
