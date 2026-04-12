package com.example.springdata.interview.sddata.l46;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Sd46BrowseService {

    private final Sd46ParentRepository parentRepository;

    public Sd46BrowseService(Sd46ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    @Transactional(readOnly = true)
    public int childTagsWithoutGraph() {
        int sum = 0;
        for (Sd46Parent p : parentRepository.findAll()) {
            for (Sd46Child c : p.getChildren()) {
                sum += c.getTag().length();
            }
        }
        return sum;
    }

    @Transactional(readOnly = true)
    public int childTagsWithGraph(String prefix) {
        int sum = 0;
        for (Sd46Parent p : parentRepository.findAllByNameStartingWith(prefix)) {
            for (Sd46Child c : p.getChildren()) {
                sum += c.getTag().length();
            }
        }
        return sum;
    }
}
