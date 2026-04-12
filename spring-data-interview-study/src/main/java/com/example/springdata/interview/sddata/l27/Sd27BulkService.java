package com.example.springdata.interview.sddata.l27;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Sd27BulkService {

    private final EntityManager entityManager;

    public Sd27BulkService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public int flagAllJpql() {
        return entityManager.createQuery("update Sd27BulkRow r set r.flagged = true where r.flagged = false")
                .executeUpdate();
    }
}
