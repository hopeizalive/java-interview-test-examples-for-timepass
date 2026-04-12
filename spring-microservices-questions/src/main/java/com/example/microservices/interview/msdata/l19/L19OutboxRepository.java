package com.example.microservices.interview.msdata.l19;

import org.springframework.data.jpa.repository.JpaRepository;

public interface L19OutboxRepository extends JpaRepository<L19OutboxEvent, Long> {}
