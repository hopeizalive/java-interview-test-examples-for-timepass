package com.example.springannotation.interview.lesson.fixtures;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * Bean Validation and MVC binder types for lessons 35–38.
 */
public final class ValidationAnnotationFixtures {

    private ValidationAnnotationFixtures() {}

    public record InputDto(@NotBlank String name) {}

    public record TripleConstraints(@NotNull String a, @NotBlank String b, @NotEmpty String c) {}

    @Validated
    public static class ValidationService {
        public void save(@Valid InputDto dto) {}
    }

    public static class BinderController {
        @org.springframework.web.bind.annotation.InitBinder
        void bind(org.springframework.web.bind.WebDataBinder binder) {
            binder.setDisallowedFields("id");
        }
    }
}
