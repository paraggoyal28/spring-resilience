package com.example.spring.ratelimiterservice.controller;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {
    private static final String ORDER_SERVICE = "orderService";

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/order")
    @RateLimiter(name=ORDER_SERVICE, fallbackMethod = "rateLimiterFallback")
    public ResponseEntity<String> createOrder(){
        String response = restTemplate.getForObject("http://localhost:8081/item", String.class);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> rateLimiterFallback(Exception e){
        return new ResponseEntity<String>("Order service does not permit further requests", HttpStatus.OK);
    }
}
