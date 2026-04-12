package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Entry;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.ldap.authentication.BindAuthenticator;

/**
 * LDAP bind authentication—embedded UnboundID directory, {@link BindAuthenticator} binds uid=test.
 */
public final class Lesson39 extends AbstractLesson {

    public Lesson39() {
        super(37, "LDAP—UnboundID in-memory server; BindAuthenticator binds uid=test.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=example,dc=com");
        config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("default", 0));
        InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);

        ds.add(new Entry(
                "dc=example,dc=com",
                new Attribute("objectClass", "top", "domain"),
                new Attribute("dc", "example")));
        ds.add(new Entry(
                "ou=people,dc=example,dc=com",
                new Attribute("objectClass", "top", "organizationalUnit"),
                new Attribute("ou", "people")));
        ds.add(new Entry(
                "uid=test,ou=people,dc=example,dc=com",
                new Attribute("objectClass", "top", "person", "organizationalPerson", "inetOrgPerson"),
                new Attribute("cn", "Test User"),
                new Attribute("sn", "User"),
                new Attribute("uid", "test"),
                new Attribute("userPassword", "secret")));

        try {
            ds.startListening();
            int port = ds.getListenPort();

            LdapContextSource cs = new LdapContextSource();
            cs.setUrl("ldap://127.0.0.1:" + port);
            cs.setBase("dc=example,dc=com");
            cs.setAnonymousReadOnly(true);
            cs.afterPropertiesSet();

            BindAuthenticator ba = new BindAuthenticator(cs);
            ba.setUserDnPatterns(new String[] { "uid={0},ou=people" });
            ba.afterPropertiesSet();
            var ctxOps = ba.authenticate(new UsernamePasswordAuthenticationToken("test", "secret"));
            if (!"test".equals(ctxOps.getStringAttribute("uid"))) {
                throw new IllegalStateException("ldap uid");
            }
            System.out.println("LDAP bind OK for uid=" + ctxOps.getStringAttribute("uid"));
        } finally {
            ds.shutDown(true);
        }
    }
}
