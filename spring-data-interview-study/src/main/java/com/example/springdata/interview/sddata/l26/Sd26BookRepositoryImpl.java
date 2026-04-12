package com.example.springdata.interview.sddata.l26;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

public class Sd26BookRepositoryImpl implements Sd26BookRepositoryCustom {

    private final EntityManager entityManager;

    public Sd26BookRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public int bumpAllPageCounts(int delta) {
        return entityManager
                .createQuery("update Sd26Book b set b.pageCount = b.pageCount + :d")
                .setParameter("d", delta)
                .executeUpdate();
    }
}
