package com.example.springdata.interview.sddata.l05;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Sd05TaskRepository extends JpaRepository<Sd05Task, Long> {

    @Query("select t from Sd05Task t where t.assignee = :who order by t.title")
    List<Sd05Task> findAssignedJpql(@Param("who") String assignee);
}
