package com.example.swiftdataapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/test")
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> getHelloMessage() {
        return ResponseEntity.ok(Map.of("message", "Hello from SWIFT API!"));
    }
}
