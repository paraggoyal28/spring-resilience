package com.example.spring.itemservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemServiceController {

    @GetMapping("/item")
    public String getItem() {
        return "Item Selected";
    }
}
