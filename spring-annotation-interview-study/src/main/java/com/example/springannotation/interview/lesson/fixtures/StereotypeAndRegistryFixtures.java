package com.example.springannotation.interview.lesson.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Spring types for lessons 1–10: stereotypes, {@code @Bean} vs component, {@code @Configuration} proxying,
 * composition metadata, {@code @Import}, {@code @Primary}/{@code @Qualifier}, injection style, {@code @Lazy}.
 *
 * <p>All nested types are {@code public static} so the container can CGLIB-subclass {@code @Configuration} classes
 * (requires a visible no-arg constructor on the enhanced class).
 */
public final class StereotypeAndRegistryFixtures {

    private StereotypeAndRegistryFixtures() {}

    @Component("genericComponent")
    public static class GenericComponent {}

    @Service
    public static class LayeredService {}

    @Repository
    public static class LayeredRepository {}

    @Configuration
    public static class BeanConfig {
        @Bean
        String factoryCreated() {
            return "bean";
        }
    }

    public record SharedDep(String name) {}

    public record PairHolder(SharedDep left, SharedDep right) {}

    @Configuration
    public static class ConfigInterCall {
        @Bean
        SharedDep dep() {
            return new SharedDep("x");
        }

        @Bean
        PairHolder pair() {
            return new PairHolder(dep(), dep());
        }
    }

    @Configuration(proxyBeanMethods = true)
    public static class ProxyTrueConfig {
        @Bean
        SharedDep dep() {
            return new SharedDep("t");
        }

        @Bean
        PairHolder pair() {
            return new PairHolder(dep(), dep());
        }
    }

    @Configuration(proxyBeanMethods = false)
    public static class ProxyFalseConfig {
        @Bean
        SharedDep dep() {
            return new SharedDep("f");
        }

        @Bean
        PairHolder pair() {
            return new PairHolder(dep(), dep());
        }
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @ComponentScan
    public static class ExplicitBootLike {}

    @ComponentScan
    public static class DefaultScanRoot {}

    @ComponentScan(basePackages = "com.example.springannotation.interview.lesson.fixtures")
    public static class ExplicitScanRoot {}

    @Configuration
    @Import(ImportedConfig.class)
    public static class ImportedRoot {}

    @Configuration
    public static class ImportedConfig {
        @Bean
        String importedOnlyBean() {
            return "imported";
        }
    }

    public interface Sender {
        String name();
    }

    public static class EmailSender implements Sender {
        @Override
        public String name() {
            return "email";
        }
    }

    public static class SmsSender implements Sender {
        @Override
        public String name() {
            return "sms";
        }
    }

    public record MessageClient(Sender sender) {}

    @Configuration
    public static class QualifierConfig {
        @Bean
        @Primary
        Sender emailSender() {
            return new EmailSender();
        }

        @Bean
        Sender smsSender() {
            return new SmsSender();
        }

        @Bean
        MessageClient messageClient(@Qualifier("smsSender") Sender sender) {
            return new MessageClient(sender);
        }
    }

    public static class FieldInjected {
        @Autowired
        private Sender sender;
    }

    public static class ConstructorInjected {
        private final Sender sender;

        public ConstructorInjected(Sender sender) {
            this.sender = sender;
        }
    }

    @Configuration
    public static class LazyConfig {
        @Bean
        AtomicInteger counter() {
            return new AtomicInteger();
        }

        @Bean
        @Lazy
        ExpensiveService expensive(AtomicInteger counter) {
            return new ExpensiveService(counter);
        }

        @Bean
        LazyConsumer lazyConsumer(@Lazy ExpensiveService expensive) {
            return new LazyConsumer(expensive);
        }
    }

    public static class ExpensiveService {
        private final AtomicInteger counter;

        public ExpensiveService(AtomicInteger counter) {
            this.counter = counter;
            this.counter.incrementAndGet();
        }

        public String value() {
            return "ok";
        }
    }

    public record LazyConsumer(ExpensiveService expensive) {
        public void touch() {
            expensive.value();
        }
    }
}
