package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/** JDBC ACL—{@link JdbcMutableAclService} persists ACE; {@code Acl#isGranted} checks reader SID. */
public final class Lesson45 extends AbstractLesson {

    public Lesson45() {
        super(44, "JdbcMutableAclService + HSQL—grant READ to ROLE_READER; isGranted true.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        // H2 2.x removed IDENTITY()/SCOPE_IDENTITY() used by Spring ACL defaults; HSQL matches those defaults.
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:acl-schema.sql")
                .build();
        try {
            var authStrategy = new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ACL_ADMIN"));
            var granting = new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
            var cache = new SpringCacheBasedAclCache(new ConcurrentMapCache("acl"), granting, authStrategy);
            var lookup = new BasicLookupStrategy(db, cache, authStrategy, granting);
            var aclService = new JdbcMutableAclService(db, lookup, cache);
            var tx = new TransactionTemplate(new DataSourceTransactionManager(db));

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    "acl-admin", "n/a", List.of(new SimpleGrantedAuthority("ROLE_ACL_ADMIN"))));
            try {
                ObjectIdentity oi = new ObjectIdentityImpl(String.class.getName(), "doc-99");
                Sid reader = new GrantedAuthoritySid("ROLE_READER");
                tx.executeWithoutResult(status -> {
                    MutableAcl acl = aclService.createAcl(oi);
                    acl.insertAce(0, BasePermission.READ, reader, true);
                    aclService.updateAcl(acl);
                });

                Acl loaded = aclService.readAclById(oi);
                boolean ok = loaded.isGranted(List.<Permission>of(BasePermission.READ), List.of(reader), false);
                if (!ok) {
                    throw new IllegalStateException("expected ACL grant READ");
                }
                System.out.println("ACL granted READ for ROLE_READER on doc-99");
            } finally {
                SecurityContextHolder.clearContext();
            }
        } finally {
            db.shutdown();
        }
    }
}
