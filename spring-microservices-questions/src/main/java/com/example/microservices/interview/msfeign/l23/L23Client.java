package com.example.microservices.interview.msfeign.l23;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "peer", url = "${peer.base-url}")
public interface L23Client {

    @GetMapping("/hi")
    String hi();
}
