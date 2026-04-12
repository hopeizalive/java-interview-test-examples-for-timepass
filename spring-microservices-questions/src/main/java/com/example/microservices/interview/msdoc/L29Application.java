package com.example.microservices.interview.msdoc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class L29Application {

    @GetMapping("/hello")
    String hello() {
        return "openapi-demo";
    }
}
