package com.example.springdata.interview.sddata.l14;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Sd14BlogPostDemoService {

    private final Sd14BlogPostRepository repository;

    public Sd14BlogPostDemoService(Sd14BlogPostRepository repository) {
        this.repository = repository;
    }

    /** Lazy collection loads via extra SQL when touched (typical N+1 pattern for multiple parents). */
    @Transactional(readOnly = true)
    public int commentCountWithoutGraph(String title) {
        Sd14BlogPost post = repository.findByTitle(title).orElseThrow();
        return post.getComments().size();
    }

    @Transactional(readOnly = true)
    public int commentCountWithEntityGraph(String title) {
        Sd14BlogPost post = repository.findWithCommentsByTitle(title).orElseThrow();
        return post.getComments().size();
    }
}
