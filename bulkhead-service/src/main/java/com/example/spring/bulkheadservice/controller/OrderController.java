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
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private static final String ORDER_SERVICE = "orderService";

    @Autowired
    RestTemplate restTemplate;
    private int attempts = 0;

    @GetMapping("/order")
    @Bulkhead(name=ORDER_SERVICE, type = Bulkhead.Type.THREADPOOL, fallbackMethod="bulkHeadFallback")
    public CompletableFuture<ResponseEntity<String>> createOrder() {
        return CompletableFuture.supplyAsync(() -> {
            // This block runs inside the Resilience4j Bulkhead Thread Pool
            String response = restTemplate.getForObject("http://localhost:8081/item", String.class);
            logger.info(LocalTime.now() + " Call processing finished = " + Thread.currentThread().getName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        });
    }

    public CompletableFuture<ResponseEntity<String>> bulkHeadFallback(Exception e) {
        logger.warn("Bulkhead tripped! Reason: {}", e.getMessage());

        // Return a graceful 429 Too Many Requests response
        return CompletableFuture.completedFuture(
                new ResponseEntity<>("Server is busy. Please try again later.", HttpStatus.TOO_MANY_REQUESTS)
        );
    }
}
