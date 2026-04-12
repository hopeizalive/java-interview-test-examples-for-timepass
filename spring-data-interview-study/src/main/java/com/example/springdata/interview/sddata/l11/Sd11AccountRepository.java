package com.example.springdata.interview.sddata.l11;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Sd11AccountRepository extends JpaRepository<Sd11Account, Long> {

    List<Sd11UsernameProjection> findByActiveTrue();
}
