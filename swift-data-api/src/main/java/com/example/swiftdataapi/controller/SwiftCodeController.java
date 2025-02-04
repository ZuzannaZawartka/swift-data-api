package com.example.swiftdataapi.controller;

import com.example.swiftdataapi.dto.BranchSwiftCodeDTO;
import com.example.swiftdataapi.model.SwiftCodeEntity;
import com.example.swiftdataapi.service.SwiftTableManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController {

    private final SwiftTableManager swiftTableManager;

    public SwiftCodeController(SwiftTableManager swiftTableManager) {
        this.swiftTableManager = swiftTableManager;
    }

    @GetMapping("/load-excel")
    public ResponseEntity<String> loadExcel() {
        swiftTableManager.loadSwiftDataFromExcel("src/main/resources/Interns_2025_SWIFT_CODES.xlsx");
        return ResponseEntity.ok("Dane zostały załadowane.");
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<Object> getSwiftCodeForCountry(@PathVariable("countryISO2code") String countryISO2code) {
        return ResponseEntity.ok(swiftTableManager.getSwiftCodesForCountry(countryISO2code));
    }

    @GetMapping("/{swiftCode}")
    public ResponseEntity<Object> getSwiftCodeDetails(@PathVariable("swiftCode") String swiftCode) {
        return ResponseEntity.ok(swiftTableManager.getSwiftCodeDetails(swiftCode));
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> addSwiftCode(@RequestBody BranchSwiftCodeDTO requestDTO) {
        Map<String, String> response = new HashMap<>();

        try {
            // Wywołanie metody w serwisie, aby dodać kod SWIFT
            SwiftCodeEntity savedEntity = swiftTableManager.addSwiftCode(requestDTO);

            // Zwrócenie komunikatu o sukcesie
            response.put("message", "SWIFT code successfully added.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Jeśli wystąpił błąd (np. kod SWIFT nie kończy się na "XXX"), zwrócenie błędu
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }



}