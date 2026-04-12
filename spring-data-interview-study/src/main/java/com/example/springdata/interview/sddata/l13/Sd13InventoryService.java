package com.example.springdata.interview.sddata.l13;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Sd13InventoryService {

    private final Sd13InventoryRepository repository;

    public Sd13InventoryService(Sd13InventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public int adjust(String code, int delta) {
        return repository.adjustStock(code, delta);
    }
}
