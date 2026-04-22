package com.example.springannotation.interview.lesson.fixtures;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * Transaction declaration types for lessons 27–34 (metadata and {@code @EnableTransactionManagement}).
 */
public final class TransactionAnnotationFixtures {

    private TransactionAnnotationFixtures() {}

    @Transactional(readOnly = false)
    public static class TxService {
        @Transactional(readOnly = true)
        public void fastRead() {}

        @Transactional
        public void defaultRollback() {}

        @Transactional(rollbackFor = SQLException.class)
        public void rollbackForChecked() {}

        @Transactional(propagation = Propagation.REQUIRED)
        public void required() {}

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void requiresNew() {}

        @Transactional(isolation = org.springframework.transaction.annotation.Isolation.READ_COMMITTED)
        public void readCommitted() {}

        @Transactional(isolation = org.springframework.transaction.annotation.Isolation.REPEATABLE_READ)
        public void repeatableRead() {}
    }

    @Configuration
    @EnableTransactionManagement
    public static class TxConfig {}
}
