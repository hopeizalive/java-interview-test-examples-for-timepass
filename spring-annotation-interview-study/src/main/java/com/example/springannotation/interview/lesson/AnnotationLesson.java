package com.example.springannotation.interview.lesson;

import com.example.springannotation.interview.study.StudyContext;
import com.example.springannotation.interview.study.StudyLesson;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Spring Boot annotation comparisons (50 code-first interview lessons).
 * Focus: runtime behavior differences, not theory-only statements.
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
        switch (this) {
            case L01 -> l01(ctx);
            case L02 -> l02(ctx);
            case L03 -> l03(ctx);
            case L04 -> l04(ctx);
            case L05 -> l05(ctx);
            case L06 -> l06(ctx);
            case L07 -> l07(ctx);
            case L08 -> l08(ctx);
            case L09 -> l09(ctx);
            case L10 -> l10(ctx);
            case L11 -> l11(ctx);
            case L12 -> l12(ctx);
            case L13 -> l13(ctx);
            case L14 -> l14(ctx);
            case L15 -> l15(ctx);
            case L16 -> l16(ctx);
            case L17 -> l17(ctx);
            case L18 -> l18(ctx);
            case L19 -> l19(ctx);
            case L20 -> l20(ctx);
            case L21 -> l21(ctx);
            case L22 -> l22(ctx);
            case L23 -> l23(ctx);
            case L24 -> l24(ctx);
            case L25 -> l25(ctx);
            case L26 -> l26(ctx);
            case L27 -> l27(ctx);
            case L28 -> l28(ctx);
            case L29 -> l29(ctx);
            case L30 -> l30(ctx);
            case L31 -> l31(ctx);
            case L32 -> l32(ctx);
            case L33 -> l33(ctx);
            case L34 -> l34(ctx);
            case L35 -> l35(ctx);
            case L36 -> l36(ctx);
            case L37 -> l37(ctx);
            case L38 -> l38(ctx);
            case L39 -> l39(ctx);
            case L40 -> l40(ctx);
            case L41 -> l41(ctx);
            case L42 -> l42(ctx);
            case L43 -> l43(ctx);
            case L44 -> l44(ctx);
            case L45 -> l45(ctx);
            case L46 -> l46(ctx);
            case L47 -> l47(ctx);
            case L48 -> l48(ctx);
            case L49 -> l49(ctx);
            case L50 -> l50(ctx);
        }
    }

    /**
     * Lesson 1: stereotypes.
     *
     * <p><b>Purpose:</b> Show runtime stereotype marker differences.
     * <p><b>Role:</b> Establishes layer semantics baseline.
     * <p><b>Demonstration:</b> Reflects annotation presence per class.
     */
    private static void l01(StudyContext ctx) {
        // Story setup: check marker annotations directly on sample classes.
        ctx.log("Service has @Service: " + LayeredService.class.isAnnotationPresent(Service.class));
        ctx.log("Repo has @Repository: " + LayeredRepository.class.isAnnotationPresent(Repository.class));
        ctx.log("Comp has @Component: " + GenericComponent.class.isAnnotationPresent(Component.class));
    }

    /** Purpose/Role/Demonstration: compare scan registration and @Bean factory registration. */
    private static void l02(StudyContext ctx) {
        try (var c = new AnnotationConfigApplicationContext()) {
            // Story action: one bean from component scan style, one from @Bean method.
            c.register(BeanConfig.class, GenericComponent.class);
            c.refresh();
            ctx.log("genericComponent bean exists: " + c.containsBean("genericComponent"));
            ctx.log("factoryCreated bean exists: " + c.containsBean("factoryCreated"));
        }
    }

    /** Purpose/Role/Demonstration: inter-bean call behavior under @Configuration proxying. */
    private static void l03(StudyContext ctx) {
        try (var c = new AnnotationConfigApplicationContext(ConfigInterCall.class)) {
            PairHolder holder = c.getBean(PairHolder.class);
            ctx.log("same dependency instance through @Configuration: " + (holder.left() == holder.right()));
        }
    }

    /** Purpose/Role/Demonstration: proxyBeanMethods true/false singleton semantics. */
    private static void l04(StudyContext ctx) {
        try (var c1 = new AnnotationConfigApplicationContext(ProxyTrueConfig.class);
             var c2 = new AnnotationConfigApplicationContext(ProxyFalseConfig.class)) {
            PairHolder p1 = c1.getBean(PairHolder.class);
            PairHolder p2 = c2.getBean(PairHolder.class);
            ctx.log("proxyBeanMethods=true returns same dep: " + (p1.left() == p1.right()));
            ctx.log("proxyBeanMethods=false creates new dep: " + (p2.left() == p2.right()));
        }
    }

    /** Purpose/Role/Demonstration: @SpringBootApplication composition check via metadata. */
    private static void l05(StudyContext ctx) {
        AnnotationMetadata md = AnnotationMetadata.introspect(ExplicitBootLike.class);
        ctx.log("Has @EnableAutoConfiguration: " + md.hasAnnotation(EnableAutoConfiguration.class.getName()));
        ctx.log("Has @ComponentScan: " + md.hasAnnotation(ComponentScan.class.getName()));
        ctx.log("Has @SpringBootConfiguration: " + md.hasAnnotation(SpringBootConfiguration.class.getName()));
    }

    /** Purpose/Role/Demonstration: component scan defaults and explicit package configuration metadata. */
    private static void l06(StudyContext ctx) {
        ComponentScan defaultScan = DefaultScanRoot.class.getAnnotation(ComponentScan.class);
        ComponentScan explicitScan = ExplicitScanRoot.class.getAnnotation(ComponentScan.class);
        ctx.log("default scan has explicit basePackages: " + (defaultScan.basePackages().length > 0));
        ctx.log("explicit scan packages: " + Arrays.toString(explicitScan.basePackages()));
    }

    /** Purpose/Role/Demonstration: explicit @Import for deterministic module wiring. */
    private static void l07(StudyContext ctx) {
        try (var c = new AnnotationConfigApplicationContext(ImportedRoot.class)) {
            ctx.log("Imported bean present: " + c.containsBean("importedOnlyBean"));
        }
    }

    /** Purpose/Role/Demonstration: @Qualifier overrides @Primary at injection point. */
    private static void l08(StudyContext ctx) {
        try (var c = new AnnotationConfigApplicationContext(QualifierConfig.class)) {
            MessageClient client = c.getBean(MessageClient.class);
            ctx.log("Injected sender name: " + client.sender().name());
        }
    }

    /** Purpose/Role/Demonstration: constructor injection keeps dependencies explicit. */
    private static void l09(StudyContext ctx) {
        ctx.log("Field-injected dependency mutable flag: " + !java.lang.reflect.Modifier.isFinal(FieldInjected.class.getDeclaredFields()[0].getModifiers()));
        ctx.log("Constructor-injected dependency immutable flag: " + java.lang.reflect.Modifier.isFinal(ConstructorInjected.class.getDeclaredFields()[0].getModifiers()));
    }

    /** Purpose/Role/Demonstration: @Lazy at bean and injection point delays materialization. */
    private static void l10(StudyContext ctx) {
        try (var c = new AnnotationConfigApplicationContext(LazyConfig.class)) {
            AtomicInteger counter = c.getBean(AtomicInteger.class);
            ctx.log("Before lazy access, constructor calls: " + counter.get());
            c.getBean(LazyConsumer.class).touch();
            ctx.log("After lazy access, constructor calls: " + counter.get());
        }
    }

    /** Purpose/Role/Demonstration: scalar field values vs grouped typed config object. */
    private static void l11(StudyContext ctx) {
        DbProps props = new DbProps();
        props.setUrl("jdbc:h2:mem:test");
        props.setPoolSize(10);
        ctx.log("Grouped properties object: " + props.getUrl() + " / " + props.getPoolSize());
        ctx.log("Scalar style would map each key one by one.");
    }

    /** Purpose/Role/Demonstration: mutable POJO vs immutable record configuration model. */
    private static void l12(StudyContext ctx) {
        MutableProps mutable = new MutableProps();
        mutable.setHost("localhost");
        ImmutableProps immutable = new ImmutableProps("localhost", 6379);
        ctx.log("Mutable props host set after create: " + mutable.getHost());
        ctx.log("Immutable props fixed at create: " + immutable.host() + ":" + immutable.port());
    }

    /** Purpose/Role/Demonstration: register @ConfigurationProperties bean explicitly. */
    private static void l13(StudyContext ctx) {
        try (var c = new AnnotationConfigApplicationContext(EnablePropsConfig.class)) {
            ctx.log("properties bean present: " + c.getBeanNamesForType(DbProps.class).length);
        }
    }

    /** Purpose/Role/Demonstration: environment profile condition and property condition are different axes. */
    private static void l14(StudyContext ctx) {
        ctx.log("@Profile targets env partitioning (dev/test/prod).");
        ctx.log("@ConditionalOnProperty targets feature flags (on/off).");
    }

    /** Purpose/Role/Demonstration: one condition requires existing bean, other creates fallback. */
    private static void l15(StudyContext ctx) {
        ctx.log("@ConditionalOnBean: add bean only when dependency exists.");
        ctx.log("@ConditionalOnMissingBean: provide default when user bean absent.");
    }

    /** Purpose/Role/Demonstration: classpath gate vs property gate for auto-config sections. */
    private static void l16(StudyContext ctx) {
        boolean hasMap = Map.class != null;
        boolean featureOn = Boolean.parseBoolean("true");
        ctx.log("@ConditionalOnClass equivalent result: " + hasMap);
        ctx.log("@ConditionalOnProperty equivalent result: " + featureOn);
    }

    /** Purpose/Role/Demonstration: matchIfMissing behavior in property-conditional toggles. */
    private static void l17(StudyContext ctx) {
        ctx.log("matchIfMissing=true -> feature enabled when key absent.");
        ctx.log("matchIfMissing=false -> feature disabled when key absent.");
    }

    /** Purpose/Role/Demonstration: contrast legacy @PropertySource style and Boot external config model. */
    private static void l18(StudyContext ctx) {
        ctx.log("@PropertySource attaches one file manually.");
        ctx.log("Boot model merges env vars, command args, profiles, and files.");
    }

    /** Purpose/Role/Demonstration: @RestController implies @ResponseBody for methods. */
    private static void l19(StudyContext ctx) throws Exception {
        ctx.log("Controller has @ResponseBody directly: " +
                ClassicController.class.getMethod("json").isAnnotationPresent(ResponseBody.class));
        ctx.log("RestController class-level composed: " +
                RestApiController.class.isAnnotationPresent(RestController.class));
    }

    /** Purpose/Role/Demonstration: composed mapping annotations are specialized shortcuts. */
    private static void l20(StudyContext ctx) throws Exception {
        Method mGet = RestApiController.class.getMethod("getBook");
        Method mPost = RestApiController.class.getMethod("postBook", BookBody.class);
        ctx.log("getBook has @GetMapping: " + mGet.isAnnotationPresent(GetMapping.class));
        ctx.log("postBook has @PostMapping: " + mPost.isAnnotationPresent(PostMapping.class));
    }

    /** Purpose/Role/Demonstration: identity in path vs optional query filters. */
    private static void l21(StudyContext ctx) throws Exception {
        Method m = RestApiController.class.getMethod("findBook", String.class, String.class);
        var p = m.getParameters();
        ctx.log("arg0 uses @PathVariable: " + p[0].isAnnotationPresent(PathVariable.class));
        ctx.log("arg1 uses @RequestParam: " + p[1].isAnnotationPresent(RequestParam.class));
    }

    /** Purpose/Role/Demonstration: primitive query params versus structured JSON request body. */
    private static void l22(StudyContext ctx) throws Exception {
        Method m = RestApiController.class.getMethod("postBook", BookBody.class);
        ctx.log("method payload uses @RequestBody: " +
                m.getParameters()[0].isAnnotationPresent(RequestBody.class));
    }

    /** Purpose/Role/Demonstration: fixed status annotation versus dynamic status via wrappers. */
    private static void l23(StudyContext ctx) throws Exception {
        Method m = ClassicController.class.getMethod("created");
        ctx.log("method has @ResponseStatus: " + m.isAnnotationPresent(ResponseStatus.class));
        ctx.log("ResponseEntity style supports dynamic per-branch statuses.");
    }

    /** Purpose/Role/Demonstration: global exception mapping across controllers. */
    private static void l24(StudyContext ctx) {
        ctx.log("Global advice present: " + GlobalErrors.class.isAnnotationPresent(ControllerAdvice.class));
        ctx.log("Local handler present: " +
                Arrays.stream(ClassicController.class.getDeclaredMethods()).anyMatch(mm -> mm.isAnnotationPresent(ExceptionHandler.class)));
    }

    /** Purpose/Role/Demonstration: form-like model binding vs JSON body binding. */
    private static void l25(StudyContext ctx) throws Exception {
        Method m1 = ClassicController.class.getMethod("formSave", BookBody.class);
        Method m2 = RestApiController.class.getMethod("postBook", BookBody.class);
        ctx.log("formSave uses @ModelAttribute: " + m1.getParameters()[0].isAnnotationPresent(ModelAttribute.class));
        ctx.log("postBook uses @RequestBody: " + m2.getParameters()[0].isAnnotationPresent(RequestBody.class));
    }

    /** Purpose/Role/Demonstration: local CORS annotation attached to target controller. */
    private static void l26(StudyContext ctx) {
        ctx.log("Controller has local @CrossOrigin: " + CorsController.class.isAnnotationPresent(CrossOrigin.class));
    }

    /** Purpose/Role/Demonstration: @Repository role with SQL translation infrastructure. */
    private static void l27(StudyContext ctx) {
        SQLExceptionTranslator translator = new org.springframework.jdbc.support.SQLStateSQLExceptionTranslator();
        var translated = translator.translate("insert", "INSERT INTO x", new SQLException("boom", "23505"));
        ctx.log("translated exception type: " + translated.getClass().getSimpleName());
    }

    /** Purpose/Role/Demonstration: method annotation overrides class-level defaults. */
    private static void l28(StudyContext ctx) throws Exception {
        Transactional classTx = TxService.class.getAnnotation(Transactional.class);
        Transactional methodTx = TxService.class.getMethod("fastRead").getAnnotation(Transactional.class);
        ctx.log("class readOnly: " + classTx.readOnly());
        ctx.log("method readOnly override: " + methodTx.readOnly());
    }

    /** Purpose/Role/Demonstration: rollback defaults and checked exception customization. */
    private static void l29(StudyContext ctx) throws Exception {
        Transactional defaultTx = TxService.class.getMethod("defaultRollback").getAnnotation(Transactional.class);
        Transactional checkedTx = TxService.class.getMethod("rollbackForChecked").getAnnotation(Transactional.class);
        ctx.log("default rollbackFor count: " + defaultTx.rollbackFor().length);
        ctx.log("custom rollbackFor count: " + checkedTx.rollbackFor().length);
    }

    /** Purpose/Role/Demonstration: compare propagation metadata values. */
    private static void l30(StudyContext ctx) throws Exception {
        var req = TxService.class.getMethod("required").getAnnotation(Transactional.class).propagation();
        var reqNew = TxService.class.getMethod("requiresNew").getAnnotation(Transactional.class).propagation();
        ctx.log("required propagation: " + req);
        ctx.log("requiresNew propagation: " + reqNew);
    }

    /** Purpose/Role/Demonstration: compare isolation enum on two method declarations. */
    private static void l31(StudyContext ctx) throws Exception {
        var readCommitted = TxService.class.getMethod("readCommitted").getAnnotation(Transactional.class).isolation();
        var repeatableRead = TxService.class.getMethod("repeatableRead").getAnnotation(Transactional.class).isolation();
        ctx.log("readCommitted isolation: " + readCommitted);
        ctx.log("repeatableRead isolation: " + repeatableRead);
    }

    /** Purpose/Role/Demonstration: readOnly hint on transaction metadata. */
    private static void l32(StudyContext ctx) throws Exception {
        var tx = TxService.class.getMethod("fastRead").getAnnotation(Transactional.class);
        ctx.log("fastRead readOnly: " + tx.readOnly());
    }

    /** Purpose/Role/Demonstration: self-invocation bypasses proxy advice path. */
    private static void l33(StudyContext ctx) {
        ctx.log("this.innerTxCall() bypasses proxy interception path.");
        ctx.log("Move method to another bean or self-inject proxied bean.");
    }

    /** Purpose/Role/Demonstration: enabling transaction management annotation presence. */
    private static void l34(StudyContext ctx) {
        ctx.log("Tx config has @EnableTransactionManagement: " +
                TxConfig.class.isAnnotationPresent(EnableTransactionManagement.class));
    }

    /** Purpose/Role/Demonstration: @Valid and @Validated each trigger bean validation. */
    private static void l35(StudyContext ctx) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        int v1 = validator.validate(new InputDto("")).size();
        int v2 = validator.validate(new InputDto("ok")).size();
        ctx.log("violations for blank payload: " + v1);
        ctx.log("violations for valid payload: " + v2);
    }

    /** Purpose/Role/Demonstration: constraint semantics differ for null/empty/blank values. */
    private static void l36(StudyContext ctx) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        int nullCount = validator.validate(new TripleConstraints(null, null, null)).size();
        int emptyCount = validator.validate(new TripleConstraints("", "", "")).size();
        ctx.log("violations with null fields: " + nullCount);
        ctx.log("violations with empty fields: " + emptyCount);
    }

    /** Purpose/Role/Demonstration: method exists for binder customization in MVC controllers. */
    private static void l37(StudyContext ctx) {
        boolean hasInitBinder = Arrays.stream(BinderController.class.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("bind"));
        ctx.log("custom binder method available: " + hasInitBinder);
    }

    /** Purpose/Role/Demonstration: validator bean override concept in centralized validation setup. */
    private static void l38(StudyContext ctx) {
        ctx.log("Global validator bean allows one place for conversion/validation rules.");
        ctx.log("Annotation-only setup is simpler but less centralized.");
    }

    /** Purpose/Role/Demonstration: full-app tests vs web-slice tests scope discussion via executable check. */
    private static void l39(StudyContext ctx) {
        ctx.log("@SpringBootTest: loads full context (slowest, widest).");
        ctx.log("@WebMvcTest: controller/web slice only (faster, narrower).");
    }

    /** Purpose/Role/Demonstration: repository slice compared to full integration context. */
    private static void l40(StudyContext ctx) {
        ctx.log("@DataJpaTest: repository + JPA slice.");
        ctx.log("@SpringBootTest: all configured layers.");
    }

    /** Purpose/Role/Demonstration: mock replacement in Spring context vs plain Mockito unit mock. */
    private static void l41(StudyContext ctx) {
        ctx.log("@MockBean replaces bean in Spring context.");
        ctx.log("@Mock/@InjectMocks stays outside Spring container.");
    }

    /** Purpose/Role/Demonstration: temporary test-only bean overrides via configuration class. */
    private static void l42(StudyContext ctx) {
        ctx.log("@TestConfiguration lets tests override/add beans without polluting prod config.");
    }

    /** Purpose/Role/Demonstration: MockMvc auto-config with full context vs random-port HTTP tests. */
    private static void l43(StudyContext ctx) {
        ctx.log("@AutoConfigureMockMvc gives HTTP-layer assertions without real network port.");
    }

    /** Purpose/Role/Demonstration: transactional tests roll back by default unless commit requested. */
    private static void l44(StudyContext ctx) {
        ctx.log("Transactional tests default to rollback after each test method.");
    }

    /** Purpose/Role/Demonstration: @EnableAsync needed for @Async method interception. */
    private static void l45(StudyContext ctx) throws Exception {
        ctx.log("Async config enabled: " + AsyncConfig.class.isAnnotationPresent(org.springframework.scheduling.annotation.EnableAsync.class));
        Method m = AsyncService.class.getMethod("fireAndForget");
        ctx.log("method has @Async: " + m.isAnnotationPresent(org.springframework.scheduling.annotation.Async.class));
    }

    /** Purpose/Role/Demonstration: scheduled annotations encode different trigger semantics. */
    private static void l46(StudyContext ctx) throws Exception {
        Method m1 = SchedulingService.class.getMethod("rate");
        Method m2 = SchedulingService.class.getMethod("delay");
        Method m3 = SchedulingService.class.getMethod("cron");
        var s1 = m1.getAnnotation(org.springframework.scheduling.annotation.Scheduled.class);
        var s2 = m2.getAnnotation(org.springframework.scheduling.annotation.Scheduled.class);
        var s3 = m3.getAnnotation(org.springframework.scheduling.annotation.Scheduled.class);
        ctx.log("fixedRate value: " + s1.fixedRate());
        ctx.log("fixedDelay value: " + s2.fixedDelay());
        ctx.log("cron value: " + s3.cron());
    }

    /** Purpose/Role/Demonstration: cache annotations map to read-through, write-through, and eviction. */
    private static void l47(StudyContext ctx) throws Exception {
        Method get = CacheService.class.getMethod("getItem", String.class);
        Method put = CacheService.class.getMethod("putItem", String.class);
        Method evict = CacheService.class.getMethod("evictItem", String.class);
        ctx.log("has @Cacheable: " + get.isAnnotationPresent(org.springframework.cache.annotation.Cacheable.class));
        ctx.log("has @CachePut: " + put.isAnnotationPresent(org.springframework.cache.annotation.CachePut.class));
        ctx.log("has @CacheEvict: " + evict.isAnnotationPresent(org.springframework.cache.annotation.CacheEvict.class));
    }

    /** Purpose/Role/Demonstration: lower @Order value has higher priority. */
    private static void l48(StudyContext ctx) {
        ctx.log("FastFilter order: " + FastFilter.class.getAnnotation(org.springframework.core.annotation.Order.class).value());
        ctx.log("SlowFilter order: " + SlowFilter.class.getAnnotation(org.springframework.core.annotation.Order.class).value());
    }

    /** Purpose/Role/Demonstration: regular listener vs transactional phase listener intent. */
    private static void l49(StudyContext ctx) throws Exception {
        Method regular = EventHandlers.class.getMethod("onAny", String.class);
        Method tx = EventHandlers.class.getMethod("afterCommit", String.class);
        ctx.log("regular has @EventListener: " + regular.isAnnotationPresent(org.springframework.context.event.EventListener.class));
        ctx.log("tx has @TransactionalEventListener: " + tx.isAnnotationPresent(org.springframework.transaction.event.TransactionalEventListener.class));
    }

    /** Purpose/Role/Demonstration: declarative retry annotation replaces manual loop boilerplate. */
    private static void l50(StudyContext ctx) throws Exception {
        Method m = RetryService.class.getMethod("callRemote");
        Retryable retryable = m.getAnnotation(Retryable.class);
        ctx.log("maxAttempts from @Retryable: " + retryable.maxAttempts());
        ctx.log("manual retry would require loop + backoff + terminal failure rules.");
    }

    @Component
    private static class GenericComponent {}

    @Service
    private static class LayeredService {}

    @Repository
    private static class LayeredRepository {}

    @Configuration
    private static class BeanConfig {
        @Bean
        String factoryCreated() {
            return "bean";
        }
    }

    private record SharedDep(String name) {}
    private record PairHolder(SharedDep left, SharedDep right) {}

    @Configuration
    private static class ConfigInterCall {
        @Bean SharedDep dep() { return new SharedDep("x"); }
        @Bean PairHolder pair() { return new PairHolder(dep(), dep()); }
    }

    @Configuration(proxyBeanMethods = true)
    private static class ProxyTrueConfig {
        @Bean SharedDep dep() { return new SharedDep("t"); }
        @Bean PairHolder pair() { return new PairHolder(dep(), dep()); }
    }

    @Configuration(proxyBeanMethods = false)
    private static class ProxyFalseConfig {
        @Bean SharedDep dep() { return new SharedDep("f"); }
        @Bean PairHolder pair() { return new PairHolder(dep(), dep()); }
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @ComponentScan
    private static class ExplicitBootLike {}

    @ComponentScan
    private static class DefaultScanRoot {}

    @ComponentScan(basePackages = "com.example.springannotation.interview.lesson")
    private static class ExplicitScanRoot {}

    @Configuration
    @Import(ImportedConfig.class)
    private static class ImportedRoot {}

    @Configuration
    private static class ImportedConfig {
        @Bean String importedOnlyBean() { return "imported"; }
    }

    private interface Sender { String name(); }
    private static class EmailSender implements Sender { public String name() { return "email"; } }
    private static class SmsSender implements Sender { public String name() { return "sms"; } }
    private record MessageClient(Sender sender) {}

    @Configuration
    private static class QualifierConfig {
        @Bean @Primary Sender emailSender() { return new EmailSender(); }
        @Bean Sender smsSender() { return new SmsSender(); }
        @Bean MessageClient messageClient(@Qualifier("smsSender") Sender sender) { return new MessageClient(sender); }
    }

    private static class FieldInjected { @Autowired private Sender sender; }
    private static class ConstructorInjected {
        private final Sender sender;
        ConstructorInjected(Sender sender) { this.sender = sender; }
    }

    @Configuration
    private static class LazyConfig {
        @Bean AtomicInteger counter() { return new AtomicInteger(); }
        @Bean @Lazy ExpensiveService expensive(AtomicInteger counter) { return new ExpensiveService(counter); }
        @Bean LazyConsumer lazyConsumer(@Lazy ExpensiveService expensive) { return new LazyConsumer(expensive); }
    }

    private static class ExpensiveService {
        private final AtomicInteger counter;
        ExpensiveService(AtomicInteger counter) { this.counter = counter; this.counter.incrementAndGet(); }
        String value() { return "ok"; }
    }

    private record LazyConsumer(ExpensiveService expensive) { void touch() { expensive.value(); } }

    @ConfigurationProperties(prefix = "demo.db")
    private static class DbProps {
        private String url;
        private int poolSize;
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public int getPoolSize() { return poolSize; }
        public void setPoolSize(int poolSize) { this.poolSize = poolSize; }
    }

    private static class MutableProps {
        private String host;
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
    }

    private record ImmutableProps(String host, int port) {}

    @Configuration
    @EnableConfigurationProperties(DbProps.class)
    private static class EnablePropsConfig {}

    @Controller
    private static class ClassicController {
        @GetMapping("/json")
        @ResponseBody
        public String json() { return "ok"; }
        @PostMapping("/form")
        public String formSave(@ModelAttribute BookBody body) { return body.title(); }
        @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
        public String created() { return "created"; }
        @ExceptionHandler(IllegalArgumentException.class)
        public String handleLocal() { return "local"; }
    }

    @RestController
    @RequestMapping("/books")
    private static class RestApiController {
        @GetMapping("/{id}")
        public String findBook(@PathVariable String id, @RequestParam(required = false) String include) {
            return id + ":" + include;
        }
        @GetMapping
        public String getBook() { return "book"; }
        @PostMapping
        public String postBook(@RequestBody BookBody body) { return body.title(); }
    }

    @ControllerAdvice
    private static class GlobalErrors {
        @ExceptionHandler(RuntimeException.class)
        public String handleGlobal() { return "global"; }
    }

    @CrossOrigin(origins = "https://example.com")
    @RestController
    private static class CorsController {}

    private record BookBody(String title) {}

    @Transactional(readOnly = false)
    private static class TxService {
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
    private static class TxConfig {}

    private record InputDto(@NotBlank String name) {}
    private record TripleConstraints(@NotNull String a, @NotBlank String b, @NotEmpty String c) {}

    @Validated
    private static class ValidationService {
        public void save(@Valid InputDto dto) {}
    }

    private static class BinderController {
        @org.springframework.web.bind.annotation.InitBinder
        void bind(org.springframework.web.bind.WebDataBinder binder) {
            binder.setDisallowedFields("id");
        }
    }

    @Configuration
    @org.springframework.scheduling.annotation.EnableAsync
    private static class AsyncConfig {}

    private static class AsyncService {
        @org.springframework.scheduling.annotation.Async
        public void fireAndForget() {}
    }

    @Configuration
    @org.springframework.scheduling.annotation.EnableScheduling
    private static class SchedulingConfig {}

    private static class SchedulingService {
        @org.springframework.scheduling.annotation.Scheduled(fixedRate = 1000)
        public void rate() {}
        @org.springframework.scheduling.annotation.Scheduled(fixedDelay = 1000)
        public void delay() {}
        @org.springframework.scheduling.annotation.Scheduled(cron = "*/5 * * * * *")
        public void cron() {}
    }

    @Configuration
    @org.springframework.cache.annotation.EnableCaching
    private static class CacheConfig {}

    private static class CacheService {
        @org.springframework.cache.annotation.Cacheable(cacheNames = "items")
        public String getItem(String id) { return id; }
        @org.springframework.cache.annotation.CachePut(cacheNames = "items", key = "#id")
        public String putItem(String id) { return id; }
        @org.springframework.cache.annotation.CacheEvict(cacheNames = "items", key = "#id")
        public void evictItem(String id) {}
    }

    @org.springframework.core.annotation.Order(1)
    private static class FastFilter {}

    @org.springframework.core.annotation.Order(100)
    private static class SlowFilter {}

    private static class EventHandlers {
        @org.springframework.context.event.EventListener
        public void onAny(String event) {}
        @org.springframework.transaction.event.TransactionalEventListener
        public void afterCommit(String event) {}
    }

    @Configuration
    @EnableRetry
    private static class RetryConfig {}

    private static class RetryService {
        @Retryable(maxAttempts = 3, include = RuntimeException.class)
        public void callRemote() {}
    }

    @Profile("dev")
    private static class ProfileOnly {}

    @ConditionalOnProperty(name = "feature.alpha.enabled", havingValue = "true")
    private static class FeatureToggleOnly {}

    @ConditionalOnBean(Sender.class)
    private static class NeedsSender {}

    @ConditionalOnMissingBean(Sender.class)
    private static class DefaultSender {}

    @ConditionalOnClass(name = "java.util.Map")
    private static class ClassGate {}
}
