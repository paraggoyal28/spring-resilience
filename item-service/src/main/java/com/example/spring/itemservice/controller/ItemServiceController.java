package com.example.spring.itemservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemServiceController {

    private final Logger logger = LoggerFactory.getLogger("ItemServiceController");

    @GetMapping("/item")
    public String getItem() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            logger.error("getItem call returned {}", e.getStackTrace());
        }
        logger.info("getItem call returned");
        return "Item Selected Successfullt";
    }
}
