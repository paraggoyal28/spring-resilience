package com.example.spring.bulkheadservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class BulkheadServiceApplication {

    public static void main(String[] args) {


        SpringApplication.run(BulkheadServiceApplication.class, args);
        List<CompletableFuture<Void>> futures = IntStream.range(1, 11) // 10 calls
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        String response = new RestTemplate().getForObject("http://localhost:8080/order", String.class);
                        System.out.println("Call " + i + " Succeeded: " + response);
                    } catch (Exception e) {
                        System.err.println("Call " + i + " Failed: " + e.getMessage());
                    }
                }))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

/*
        SpringApplication.run(BulkheadServiceApplication.class, args);
        List<CompletableFuture<Void>> futuresSem = IntStream.range(1, 11) // 10 calls
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        String response = new RestTemplate().getForObject("http://localhost:8080/orderSemaphore", String.class);
                        System.out.println("Call " + i + " Succeeded: " + response);
                    } catch (Exception e) {
                        System.err.println("Call " + i + " Failed: " + e.getMessage());
                    }
                }))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futuresSem.toArray(new CompletableFuture[0])).join();
*/
    }

}
