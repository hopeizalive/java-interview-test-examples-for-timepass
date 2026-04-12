package com.example.microservices.interview.support;

import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.GenericWebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/** Web {@link GenericWebApplicationContext} + {@link MockMvc} with Spring Security filter chain. */
public final class SecurityWebHarness implements AutoCloseable {

    private final GenericWebApplicationContext ctx;

    public SecurityWebHarness(Class<?>... configClasses) {
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
        return MockMvcBuilders.webAppContextSetup(ctx).apply(springSecurity()).build();
    }

    @Override
    public void close() {
        ctx.close();
    }
}
