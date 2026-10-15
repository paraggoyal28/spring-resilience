package com.example.spring.timelimiterservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SlowService {
    private static final Logger logger = LoggerFactory.getLogger(SlowService.class);

    public String slowMethod() {
        logger.info("slowMethod going to sleep for 30 seconds...");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return "Slow service invoked successfully";
    }
}
