package com.example.microservices.interview.ms50a;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class Ms50AFacadeController {

    private final RestClient peer;

    public Ms50AFacadeController(@Value("${ms50.peer}") String peerBase) {
        this.peer = RestClient.builder().baseUrl(peerBase).build();
    }

    @GetMapping("/via-b")
    public String viaB() {
        return peer.get().uri("/b/ping").retrieve().body(String.class);
    }
}
