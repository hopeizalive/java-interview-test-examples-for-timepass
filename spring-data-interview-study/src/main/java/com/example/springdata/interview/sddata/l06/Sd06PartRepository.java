package com.example.springdata.interview.sddata.l06;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Sd06PartRepository extends JpaRepository<Sd06Part, Long> {

    @Query(value = "select * from sd06_part", nativeQuery = true)
    Page<Sd06Part> findAllNative(Pageable pageable);
}
