package com.example.springannotation.interview.lesson.fixtures;

import org.springframework.stereotype.Controller;
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

/**
 * MVC-style types for lessons 19–26: {@code @Controller}/{@code @RestController}, mapping shortcuts,
 * parameter binding annotations, {@code @ControllerAdvice}, {@code @CrossOrigin}.
 */
public final class WebMvcControllerFixtures {

    private WebMvcControllerFixtures() {}

    @Controller
    public static class ClassicController {
        @GetMapping("/json")
        @ResponseBody
        public String json() {
            return "ok";
        }

        @PostMapping("/form")
        public String formSave(@ModelAttribute BookBody body) {
            return body.title();
        }

        @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
        public String created() {
            return "created";
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public String handleLocal() {
            return "local";
        }
    }

    @RestController
    @RequestMapping("/books")
    public static class RestApiController {
        @GetMapping("/{id}")
        public String findBook(@PathVariable String id, @RequestParam(required = false) String include) {
            return id + ":" + include;
        }

        @GetMapping
        public String getBook() {
            return "book";
        }

        @PostMapping
        public String postBook(@RequestBody BookBody body) {
            return body.title();
        }
    }

    @ControllerAdvice
    public static class GlobalErrors {
        @ExceptionHandler(RuntimeException.class)
        public String handleGlobal() {
            return "global";
        }
    }

    @CrossOrigin(origins = "https://example.com")
    @RestController
    public static class CorsController {}

    public record BookBody(String title) {}
}
