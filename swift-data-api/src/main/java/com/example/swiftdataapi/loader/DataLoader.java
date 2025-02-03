package com.example.swiftdataapi.loader;

import com.example.swiftdataapi.service.SwiftTableManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final SwiftTableManager swiftTableManager;

    public DataLoader(SwiftTableManager swiftTableManager) {
        this.swiftTableManager = swiftTableManager;
    }

    @Override
    public void run(String... args) {
        // Wywołanie metody wczytującej dane z Excela
        swiftTableManager.loadSwiftDataFromExcel("src/main/resources/Interns_2025_SWIFT_CODES.xlsx");
    }
}
