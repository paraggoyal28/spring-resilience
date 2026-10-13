package com.example.spring.bulkheadservice.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

@RestController
public class OrderSemaphoreController {
    private static final Logger logger = LoggerFactory.getLogger(OrderSemaphoreController.class);
    private static final String ORDER_SERVICE = "orderSemaphoreService";

    @Autowired
    RestTemplate restTemplate;
    private int attempts = 0;

    @GetMapping("/orderSemaphore")
    @Bulkhead(name=ORDER_SERVICE, fallbackMethod="semaphoreFallback")
    public ResponseEntity<String> createOrder() {
        // This block runs inside the Resilience4j Bulkhead Thread Pool
        String response = restTemplate.getForObject("http://localhost:8081/item", String.class);
        logger.info(LocalTime.now() + " Call processing finished = " + Thread.currentThread().getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> semaphoreFallback(Exception e) {
        return new ResponseEntity<>("Service busy (Semaphore active).", HttpStatus.TOO_MANY_REQUESTS);
    }
}