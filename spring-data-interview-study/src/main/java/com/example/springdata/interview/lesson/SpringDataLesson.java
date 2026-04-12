package com.example.springdata.interview.lesson;

import com.example.springdata.interview.sddata.LessonRuntime;
import com.example.springdata.interview.study.StudyContext;
import com.example.springdata.interview.study.StudyLesson;
import com.example.springdata.interview.support.DataSdBoot;
import org.springframework.boot.WebApplicationType;

/**
 * Spring Data lessons with runnable persistence code only (no pure narrative slots).
 * Lessons renumbered 1–34 after removing theory-only entries.
 */
public enum SpringDataLesson implements StudyLesson {

    L01(1, "Spring Data JpaRepository vs EntityManager — less ceremony for CRUD."),
    L02(2, "JpaRepository layers CrudRepository + PagingAndSortingRepository + JPA helpers."),
    L03(3, "Repository proxies: Spring Data → JDK proxy → SimpleJpaRepository."),
    L04(4, "Query derivation from method names — and when names become unreadable."),
    L05(5, "@Query (JPQL) when derivation is unclear or you need explicit JPQL."),
    L06(6, "Native SQL: power vs dialect, pagination, and portability."),
    L07(7, "Page<T> runs a count query; Slice<T> only checks if a next page exists."),
    L08(8, "Spring MVC binds Pageable from query params (page, size, sort)."),
    L09(9, "Dynamic Sort via Sort.by without encoding sort fields in the method name."),
    L10(10, "Query by Example: probe entities; weak for OR-heavy searches."),
    L11(11, "Interface projections return only selected columns."),
    L12(12, "JPQL constructor expressions for DTO / class-based reads."),
    L13(13, "@Modifying + clearAutomatically / flush for bulk JPQL updates."),
    L14(14, "@EntityGraph reduces N+1 when loading associations."),
    L15(15, "LazyInitializationException: touch lazy fields inside @Transactional."),
    L16(16, "Keep @Transactional on services; repositories stay persistence-focused."),
    L17(17, "save(): persist vs merge depending on whether the entity is new or detached."),
    L18(18, "saveAll + JDBC batch properties for bulk inserts."),
    L19(19, "Specifications compose dynamic predicates without stringly JPQL."),
    L20(20, "Optimistic locking with @Version and ObjectOptimisticLockingFailureException."),
    L21(21, "flush() sends SQL early; still within the same transaction boundary."),
    L22(22, "@Transactional(readOnly=true) for read-heavy service methods."),
    L23(23, "getReferenceById: lazy proxy; findById: Optional with round-trip when absent."),
    L24(24, "CascadeType + orphanRemoval shape aggregate deletes."),
    L25(25, "JPA auditing: @CreatedDate / @LastModifiedDate via EntityListeners."),
    L26(26, "Custom repository fragment + *Impl registered by naming convention."),
    L27(27, "EntityManager.executeUpdate for bulk operations beside repositories."),
    L28(28, "Pagination + join fetch: duplicates, distinct, separate countQuery."),
    L29(29, "Spring Data JDBC: small aggregates, explicit SQL, no lazy navigation."),
    L30(30, "SpEL in @Query (#{#…}) adds flexibility but hurts static analysis."),
    L31(31, "Domain events (AbstractAggregateRoot) publish after successful persist."),
    L32(32, "Avoid N+1: fetch join, @EntityGraph, or projections for API payloads."),
    L33(33, "Indexes + selective predicates align repository methods with planner use."),
    L34(34, "Unique constraints + DataIntegrityViolationException for idempotent writes.");

    public static final int LESSON_COUNT = 34;

    private final int number;
    private final String title;

    SpringDataLesson(int number, String title) {
        this.number = number;
        this.title = title;
    }

    @Override
    public int number() {
        return number;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public void run(StudyContext ctx) throws Exception {
        String db = "sd" + String.format("%02d", number);
        WebApplicationType web = number == 8 ? WebApplicationType.SERVLET : WebApplicationType.NONE;
        try (var c = DataSdBoot.startStudy(db, web)) {
            c.getBean(LessonRuntime.class).runLesson(number, c, ctx);
        }
    }
}
