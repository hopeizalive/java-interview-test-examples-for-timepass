package com.example.microservices.interview.msdata.l11;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class L11UserService {

    private final L11UserRepository repo;

    public L11UserService(L11UserRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public L11User create(String name) {
        L11User u = new L11User();
        u.setName(name);
        return repo.save(u);
    }

    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}
