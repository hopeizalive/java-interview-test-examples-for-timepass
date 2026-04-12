package com.example.springdata.interview.sddata.l22;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Sd22ReportService {

    private final Sd22ReportRowRepository repository;

    public Sd22ReportService(Sd22ReportRowRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public long countRows() {
        return repository.count();
    }
}
