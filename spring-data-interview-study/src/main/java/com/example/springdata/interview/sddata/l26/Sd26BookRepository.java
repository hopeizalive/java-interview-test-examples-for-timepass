package com.example.springdata.interview.sddata.l26;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Sd26BookRepository extends JpaRepository<Sd26Book, Long>, Sd26BookRepositoryCustom {}
