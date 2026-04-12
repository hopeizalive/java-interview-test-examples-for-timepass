package com.example.microservices.interview.ms50b;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ms50BController {

    @GetMapping("/b/ping")
    public String ping() {
        return "from-b";
    }
}
