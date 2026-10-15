package com.example.spring.timelimiterservice.controller;

import com.example.spring.timelimiterservice.service.SlowService;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@RestController
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private static final String ORDER_SERVICE = "orderService";

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public SlowService slowService;

    @GetMapping("/order")
    @TimeLimiter(name = ORDER_SERVICE)
    public CompletableFuture<String> createOrder(){
        return CompletableFuture.supplyAsync(slowService::slowMethod);
    }

    public CompletableFuture<String> timelimiter_fallback(Exception e){
        return CompletableFuture.supplyAsync(() -> "Fallback method called for Timelimiter");

    }
}
