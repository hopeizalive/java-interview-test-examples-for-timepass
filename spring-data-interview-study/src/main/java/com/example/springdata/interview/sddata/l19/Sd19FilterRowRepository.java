package com.example.springdata.interview.sddata.l19;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Sd19FilterRowRepository
        extends JpaRepository<Sd19FilterRow, Long>, JpaSpecificationExecutor<Sd19FilterRow> {}
