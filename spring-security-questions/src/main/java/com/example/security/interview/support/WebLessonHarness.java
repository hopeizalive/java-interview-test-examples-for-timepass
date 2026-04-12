package com.example.security.interview.support;

import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.GenericWebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Builds a web {@link org.springframework.web.context.WebApplicationContext} with a
 * {@link MockServletContext}, then {@link MockMvc}. Uses {@link GenericWebApplicationContext}
 * (not {@link org.springframework.web.context.support.AnnotationConfigWebApplicationContext})
 * so Spring MVC 6 + Security can initialize {@code mvcHandlerMappingIntrospector} and resource
 * handler mappings without "No ServletContext set".
 */
public final class WebLessonHarness implements AutoCloseable {

    private final GenericWebApplicationContext ctx;

    public WebLessonHarness(Class<?>... configClasses) {
        ctx = new GenericWebApplicationContext();
        MockServletContext servletContext = new MockServletContext("");
        servletContext.addListener(HttpSessionEventPublisher.class);
        ctx.setServletContext(servletContext);
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
