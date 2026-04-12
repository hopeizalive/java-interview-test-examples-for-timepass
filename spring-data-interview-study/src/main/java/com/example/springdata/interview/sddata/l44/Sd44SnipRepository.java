package com.example.springdata.interview.sddata.l44;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Sd44SnipRepository extends JpaRepository<Sd44Snip, Long> {

    @Query("select s from Sd44Snip s where s.title like concat('%', :#{#needle}, '%')")
    List<Sd44Snip> searchByTitleSpel(@Param("needle") String needle);
}
