package com.example.spring.retryservice.controller;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class OrderController {
    long attempts;
    private static final String ORDER_SERVICE = "orderService";
    @Autowired
    private RestTemplate restTemplate;

    OrderController() {
        attempts = 0;
    }

    @GetMapping("/order")
    @Retry(name = ORDER_SERVICE, fallbackMethod = "fallback_retry")
    public ResponseEntity<String> createOrder(){
        log.info("item service call attempted: " + attempts++);
        String response = restTemplate.getForObject("http://localhost:8081/item", String.class);
        log.info("Item Service called");
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> fallback_retry(Exception e){
        attempts = 1;
        return new ResponseEntity<String>("Item service is down", HttpStatus.OK);
    }

}
