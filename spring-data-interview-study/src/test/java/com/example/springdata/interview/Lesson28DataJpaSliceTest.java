package com.example.springdata.interview;

import com.example.springdata.interview.sddata.l02.Sd02TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Slice test referenced from lesson 28: JPA + repositories without full web layer.
 * Run: {@code mvn -pl spring-data-interview-study test -Dtest=Lesson28DataJpaSliceTest}
 */
@DataJpaTest
class Lesson28DataJpaSliceTest {

    @Autowired
    private Sd02TagRepository tags;

    @Test
    void loadsRepositorySlice() {
        assertThat(tags).isNotNull();
    }
}
