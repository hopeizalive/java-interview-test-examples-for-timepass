package com.example.springdata.interview.sddata.l09;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Sd09SongRepository extends JpaRepository<Sd09Song, Long> {

    List<Sd09Song> findAll(Sort sort);
}
