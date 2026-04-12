package com.example.springdata.interview.sddata.l08;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sd08/items")
public class Sd08ItemController {

    private final Sd08ItemRepository repository;

    public Sd08ItemController(Sd08ItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Page<Sd08Item> page(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
