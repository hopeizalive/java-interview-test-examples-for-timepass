package com.example.springdata.interview.sddata.l14;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Sd14BlogPostRepository extends JpaRepository<Sd14BlogPost, Long> {

    Optional<Sd14BlogPost> findByTitle(String title);

    @EntityGraph(Sd14BlogPost.WITH_COMMENTS)
    @Query("select p from Sd14BlogPost p where p.title = :t")
    Optional<Sd14BlogPost> findWithCommentsByTitle(@Param("t") String title);
}
