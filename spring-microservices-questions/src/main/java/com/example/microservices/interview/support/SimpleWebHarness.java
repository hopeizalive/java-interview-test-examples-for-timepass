package com.example.microservices.interview.support;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Minimal servlet {@link org.springframework.web.context.WebApplicationContext} + {@link MockMvc}
 * without Spring Security filters.
 */
public final class SimpleWebHarness implements AutoCloseable {

    private final GenericWebApplicationContext ctx;

    public SimpleWebHarness(Class<?>... configClasses) {
        ctx = new GenericWebApplicationContext();
        ctx.setServletContext(new MockServletContext(""));
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(ctx);
        reader.register(configClasses);
        ctx.refresh();
    }

    public GenericWebApplicationContext context() {
        return ctx;
    }

    public MockMvc mockMvc() {
        var builder = MockMvcBuilders.webAppContextSetup(ctx);
        for (String name : ctx.getBeanNamesForType(Filter.class, false, true)) {
            builder.addFilters(ctx.getBean(name, Filter.class));
        }
        return builder.build();
    }

    @Override
    public void close() {
        ctx.close();
    }
}
