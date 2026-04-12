package com.example.springdata.interview.sddata.l01;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Sd01DemoService {

    private final Sd01NoteRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public Sd01DemoService(Sd01NoteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public long saveViaRepository(String text) {
        Sd01Note n = new Sd01Note();
        n.setText(text);
        return repository.save(n).getId();
    }

    @Transactional
    public long saveViaEntityManager(String text) {
        Sd01Note n = new Sd01Note();
        n.setText(text);
        entityManager.persist(n);
        entityManager.flush();
        return n.getId();
    }

    @Transactional(readOnly = true)
    public long countRepository() {
        return repository.count();
    }
}
