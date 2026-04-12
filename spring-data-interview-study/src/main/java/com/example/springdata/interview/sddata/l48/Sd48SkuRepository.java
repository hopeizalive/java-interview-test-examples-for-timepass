package com.example.springdata.interview.sddata.l48;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Sd48SkuRepository extends JpaRepository<Sd48Sku, Long> {

    Optional<Sd48Sku> findBySkuCode(String skuCode);
}
