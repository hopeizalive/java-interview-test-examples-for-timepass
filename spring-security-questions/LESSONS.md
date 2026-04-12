# Spring Security lessons 1–49

Command (from repo root):

```text
mvn -pl spring-security-questions exec:java -Dexec.args="run <n>"
```

| # | Title | What the run proves |
|---|--------|---------------------|
| 1 | Authentication vs authorization—SecurityFilterChain | Anonymous `/private` → 401; Basic `alice` → 200. |
| 2 | SecurityContextHolder | Set/clear/read `SecurityContext` around holder. |
| 3 | GrantedAuthority / roles | `ROLE_ADMIN` vs `ROLE_USER` on `/admin`. |
| 4 | ProviderManager | Custom provider handles magic password; DAO handles normal user. |
| 5 | Multiple SecurityFilterChain | `/api/**` permitAll; `/app/**` needs Basic auth. |
| 6 | permitAll for /public/** | Public OK; internal path unauthorized without auth. |
| 7 | RequestCache + form login | Redirect to login; after POST login, `/secure/data` OK in same session. |
| 8 | Filter order | Filters before/after `UsernamePasswordAuthenticationFilter` order recorded. |
| 9 | BCrypt PasswordEncoder | Encode + `matches`; wrong password fails. |
| 10 | DelegatingPasswordEncoder | `{bcrypt}` and `{noop}` both verify. |
| 11 | UserDetailsService | Found user vs `UsernameNotFoundException`. |
| 12 | DaoAuthenticationProvider | Bad password still unauthenticated; good password reaches protected GET. |
| 13 | InMemory vs custom UserDetailsService | Same `hasRole('EXTRA')`; in-memory user lacks role → 403; custom grants → 200. |
| 14 | Form login | Session authenticated after POST `/login`. |
| 15 | HTTP Basic | 401 + `WWW-Authenticate`; valid Basic → 200. |
| 16 | Logout | `/me` OK after login; after POST `/logout` with CSRF → 401. |
| 17 | Session fixation | `changeSessionId`; session id value changes after login. |
| 18 | Concurrent sessions | Second login expires first session for same user. |
| 19 | Remember-me | Remember-me cookie + fresh session still hits protected `/rm`. |
| 20 | requestMatchers + hasRole | `/u/**` with `ROLE_USER`. |
| 21 | permitAll / denyAll / authenticated | Three behaviors on three URLs. |
| 22 | fullyAuthenticated vs remember-me | `RememberMeAuthenticationToken` rejected for `/strict` via post-processor. |
| 23 | hasAuthority / hasAnyRole | `SCOPE_read` vs `hasAnyRole('ADMIN','USER')`. |
| 24 | Path `/**` pitfall | `/api/demo` without `/**` denies child path; fixed matcher allows child. |
| 25 | @EnableMethodSecurity | `@PreAuthorize('ROLE_ADMIN')` throws without admin context. |
| 26 | @PostAuthorize | Return value fails post-check → `AccessDeniedException`. |
| 27 | @Secured vs @RolesAllowed | Both require `ROLE_ADMIN`; neither satisfied in empty security context. |
| 28 | @AuthenticationPrincipal | Custom `UserDetails` subtype exposed in MVC. |
| 29 | PermissionEvaluator | `documentRead("doc1")` OK; `documentRead("doc2")` throws `AccessDeniedException`. |
| 30 | CSRF | POST without token → 403; `with(csrf())` → 200. |
| 31 | CORS | OPTIONS preflight returns `Access-Control-Allow-Origin`. |
| 32 | Security headers | `X-Frame-Options: DENY` and HSTS present. |
| 33 | JWT resource server | Valid HS256 Bearer → 200; junk token → 401. |
| 34 | JwtAuthenticationConverter | `roles` claim mapped to `ROLE_*` for authorization. |
| 35 | Client credentials | `MockWebServer` issues token JSON; token client parses access token. |
| 36 | OIDC-shaped JWT | Decode locally signed JWT with `aud` / `email`. |
| 37 | Opaque token introspector | `opaque-ok` → 200; `bad` → 401. |
| 38 | LDAP bind | UnboundID in-memory LDAP; `BindAuthenticator` binds `test` user. |
| 39 | RequestAttributeAuthenticationFilter | Request attribute becomes `PreAuthenticated` principal. |
| 40 | Entry point vs access denied | No auth → 401 body `unauth`; wrong role → 403 `denied`. |
| 41 | AccessDeniedHandler | VIP-only route returns custom 403 body. |
| 42 | RunAsUserToken | Token carries `ROLE_RUN_AS_SPECIAL`. |
| 43 | Switch user | Admin impersonates `victim`; `/who` returns victim name. |
| 44 | JDBC ACL | H2 + `JdbcMutableAclService`; `isGranted` READ for `ROLE_READER`. |
| 45 | KeyGenerators | Non-empty string and random bytes from `spring-security-crypto`. |
| 46 | `user()` vs Basic | MockMvc synthetic user and real Basic both pass `/secure`. |
| 47 | securityContext() | Injects custom `SecurityContext` into request. |
| 48 | authentication() | Injects `Authentication` into request. |
| 49 | WebFlux chain | `/api/ping` 401 anonymous; `mockUser` → 200. |

---

### Interview bullets (by area)

**Core / servlet**

- **Authentication** proves who you are; **authorization** decides what you may do. In Spring Security both travel through the `SecurityFilterChain` and `SecurityContext`.
- **`SecurityContextHolder`** is typically populated by the security filters per request; clear it in async or non-request code to avoid leaks in tests or CLI-style demos.
- **Multiple `SecurityFilterChain` beans** are ordered with `@Order`; more specific `securityMatcher` chains should usually win over the default catch-all.

**Passwords / UserDetails**

- Prefer **`PasswordEncoder`** (BCrypt, Argon2, etc.) and avoid storing plaintext. **`DelegatingPasswordEncoder`** supports `{id}` prefixes for migrations.
- **`DaoAuthenticationProvider`** combines `UserDetailsService` + `PasswordEncoder` for username/password.

**Sessions / exploits**

- **Session fixation**: use `sessionFixation().changeSessionId()` (or migrate session) on login.
- **Concurrent sessions**: `maximumSessions` + `SessionRegistry` evict or block extra sessions.
- **CSRF**: for cookie-based browsers, match CSRF token on state-changing requests; for stateless JWT APIs, CSRF is often disabled but transport and token validation must be solid.

**OAuth2 / JWT**

- **Resource server** validates JWT (signature, `exp`, `iss`, `aud` as configured). Use **`JwtAuthenticationConverter`** to map claims to `GrantedAuthority`.
- **Opaque tokens** use an **introspection** endpoint or custom `OpaqueTokenIntrospector`.

**Method security**

- **`@EnableMethodSecurity`** enables `@PreAuthorize`, `@PostAuthorize`, `@Secured`, JSR-250. SpEL can call **`PermissionEvaluator`** for fine-grained ACL-style checks.

**Reactive**

- **`SecurityWebFilterChain`** is the WebFlux analog of `SecurityFilterChain`; authorize with `authorizeExchange` and `pathMatchers`.

**Testing**

- **`@WithMockUser`** (JUnit) and **`SecurityMockMvcRequestPostProcessors`** (MockMvc) inject authentication without a full login flow.
- **WebTestClient** + **`mockUser`** exercises reactive security without a browser.
