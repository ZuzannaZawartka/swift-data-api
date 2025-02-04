package com.example.swiftdataapi.controller;

import com.example.swiftdataapi.model.SwiftCodeEntity;
import com.example.swiftdataapi.service.SwiftTableManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}