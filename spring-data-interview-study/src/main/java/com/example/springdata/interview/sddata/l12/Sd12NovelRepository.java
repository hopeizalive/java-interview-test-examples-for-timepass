package com.example.springdata.interview.sddata.l12;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Sd12NovelRepository extends JpaRepository<Sd12Novel, Long> {

    @Query(
            "select new com.example.springdata.interview.sddata.l12.Sd12NovelDto(n.title, n.author) "
                    + "from Sd12Novel n where n.author = :author")
    List<Sd12NovelDto> findDtosByAuthor(@Param("author") String author);
}
