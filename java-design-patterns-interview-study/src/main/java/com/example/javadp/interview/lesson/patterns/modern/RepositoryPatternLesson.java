package com.example.javadp.interview.lesson.patterns.modern;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository (DDD): collection-oriented persistence facade for aggregates - domain language, not SQL strings, at the
 * boundary.
 */
public final class RepositoryPatternLesson {

    private RepositoryPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "Spring Data JPA repositories",
                "UserRepository extends JpaRepository<User, Long> - CRUD speaks entities while Hibernate maps rows.",
                "Repository vs DAO: repositories are domain-centric.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "MongoDB Spring Data",
                "Document stores still expose repository interfaces so services depend on save/findByX, not BSON APIs.",
                "Same pattern, different storage engine behind the port.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Hexagonal architecture ports",
                "Application core defines OrderRepository port; adapters implement it for Postgres or in-memory tests.",
                "Enables swapping persistence without rewriting domain logic.");

        ctx.log("--- Java core tie-in ---");
        java.util.prefs.Preferences prefs =
                java.util.prefs.Preferences.userRoot().node("com.example.javadp.interview.repositoryDemo");
        prefs.put("user", "ada");
        ctx.log("java.util.prefs.Preferences is a tiny JDK key-value facade (repository-shaped): " + prefs.get("user", "missing"));
        prefs.remove("user");

        PatternLessonHeader.print(
                ctx,
                "Repository",
                "Modern / DDD persistence",
                "Hide storage details behind save/find/delete that speak the domain language (User, not JDBC row).",
                "InMemoryUserRepository stores entities in a map; client code never touches SQL.");
        UserRepository users = new InMemoryUserRepository();
        users.save(new User(1, "ada"));
        Optional<User> found = users.findById(1);
        ctx.log("Loaded user: " + found.map(User::name).orElse("missing"));
    }

    private record User(long id, String name) {}

    private interface UserRepository {
        void save(User u);

        Optional<User> findById(long id);
    }

    private static final class InMemoryUserRepository implements UserRepository {
        private final Map<Long, User> data = new HashMap<>();

        @Override
        public void save(User u) {
            data.put(u.id(), u);
        }

        @Override
        public Optional<User> findById(long id) {
            return Optional.ofNullable(data.get(id));
        }
    }
}
