# Spring Security interview lessons

Fifty **hands-on** lessons (Java demos, not prose-only). Each `run` exercises Spring Security APIs or a minimal web/reactive setup.

## Run the CLI

### Easiest on Windows (from repo root)

Use the wrapper scripts next to `mvnw.cmd` (no IntelliJ Maven path, no quoting headaches):

```text
security-study.cmd
security-study.cmd list
security-study.cmd run 11
security-study.cmd run-all
```

After a one-time package, you can run the fat JAR without Maven (fast restarts):

```text
mvnw -pl spring-security-questions package
security-study-jar.cmd list
security-study-jar.cmd run 11
```

### Maven / Java only

```text
mvnw -pl spring-security-questions exec:java -Dexec.args="list"
mvnw -pl spring-security-questions exec:java -Dexec.args="run 11"
java -jar spring-security-questions/target/spring-security-questions-1.0-SNAPSHOT.jar run-all
```

Requirements: **Java 21**, network on first build (dependency download). The module POM adds the **Shibboleth** Maven repository so OpenSAML (SAML2 support) can resolve. Some lessons start embedded LDAP or H2; no external IdP is required for the default code path.

## Lesson index and interview notes

See [LESSONS.md](LESSONS.md) for all titles, what each run proves, and bullet-point talking points for interviews.
