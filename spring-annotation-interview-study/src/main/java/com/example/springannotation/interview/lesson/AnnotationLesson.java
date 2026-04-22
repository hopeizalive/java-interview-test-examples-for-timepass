package com.example.springannotation.interview.lesson;

import com.example.springannotation.interview.study.StudyContext;
import com.example.springannotation.interview.study.StudyLesson;

/**
 * Catalog of 50 Spring Boot annotation comparison lessons (code-first, runnable via CLI).
 *
 * <p>Each constant is only metadata (number + title). Runnable stories live in {@link com.example.springannotation.interview.lesson.blocks}
 * (five blocks of ten lessons) and Spring fixture types live in {@link com.example.springannotation.interview.lesson.fixtures}.
 *
 * <p>Read the block class Javadoc first—it explains the narrative arc for that group of ten lessons.
 */
public enum AnnotationLesson implements StudyLesson {

    L01(1, "@Component vs @Service vs @Repository runtime stereotype comparison."),
    L02(2, "@Component vs @Bean registration style with explicit factory method."),
    L03(3, "@Configuration vs @Component for inter-bean method calls."),
    L04(4, "@Configuration proxyBeanMethods true vs false behavior."),
    L05(5, "@SpringBootApplication composition vs explicit three annotations."),
    L06(6, "@ComponentScan defaults vs explicit base package selection."),
    L07(7, "@Import vs scan: explicit module wiring example."),
    L08(8, "@Primary vs @Qualifier bean selection precedence."),
    L09(9, "@Autowired field injection vs constructor injection visibility."),
    L10(10, "@Lazy at bean type vs injection point."),
    L11(11, "@Value-like scalar injection vs grouped @ConfigurationProperties style."),
    L12(12, "@ConfigurationProperties mutable class vs immutable record style."),
    L13(13, "@EnableConfigurationProperties registration behavior."),
    L14(14, "@Profile vs @ConditionalOnProperty intent split."),
    L15(15, "@ConditionalOnBean vs @ConditionalOnMissingBean."),
    L16(16, "@ConditionalOnClass vs @ConditionalOnProperty gates."),
    L17(17, "@ConditionalOnProperty matchIfMissing true vs false."),
    L18(18, "@PropertySource style vs Boot-native property model."),
    L19(19, "@Controller vs @RestController implicit @ResponseBody."),
    L20(20, "@RequestMapping vs composed @GetMapping/@PostMapping."),
    L21(21, "@PathVariable vs @RequestParam method signatures."),
    L22(22, "@RequestParam vs @RequestBody payload style."),
    L23(23, "@ResponseStatus vs status through response wrapper style."),
    L24(24, "@ControllerAdvice vs local @ExceptionHandler."),
    L25(25, "@ModelAttribute vs @RequestBody binding intent."),
    L26(26, "@CrossOrigin local annotation presence."),
    L27(27, "@Repository role and SQL exception translation pathway."),
    L28(28, "@Transactional class-level vs method-level precedence."),
    L29(29, "@Transactional rollback defaults vs rollbackFor."),
    L30(30, "Propagation REQUIRED vs REQUIRES_NEW metadata comparison."),
    L31(31, "Isolation enum differences for transactional methods."),
    L32(32, "@Transactional readOnly true metadata."),
    L33(33, "Self-invocation transactional proxy trap demonstration."),
    L34(34, "@EnableTransactionManagement presence and purpose."),
    L35(35, "@Valid vs @Validated for method parameter checks."),
    L36(36, "@NotNull vs @NotBlank vs @NotEmpty."),
    L37(37, "@InitBinder extension point vs default conversion."),
    L38(38, "Global validator bean idea vs annotation-only defaults."),
    L39(39, "@SpringBootTest scope vs slice test mental model."),
    L40(40, "@DataJpaTest style scope vs full context."),
    L41(41, "@MockBean style replacement vs plain Mockito mock."),
    L42(42, "@TestConfiguration style override intent."),
    L43(43, "@AutoConfigureMockMvc style with full app context."),
    L44(44, "Transactional test rollback default concept."),
    L45(45, "@Async + @EnableAsync wiring."),
    L46(46, "@Scheduled fixedRate vs fixedDelay vs cron metadata."),
    L47(47, "@Cacheable vs @CachePut vs @CacheEvict operation intent."),
    L48(48, "@Order priority semantics."),
    L49(49, "@EventListener vs @TransactionalEventListener timing."),
    L50(50, "@Retryable usage vs manual retry loop.");

    public static final int EXPECTED_LESSON_COUNT = 50;

    private final int number;
    private final String title;

    AnnotationLesson(int number, String title) {
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
        AnnotationLessonDispatch.run(this, ctx);
    }
}
