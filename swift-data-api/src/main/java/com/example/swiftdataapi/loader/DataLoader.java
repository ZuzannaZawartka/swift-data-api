package com.example.swiftdataapi.loader;

import com.example.swiftdataapi.service.SwiftTableManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataLoader is responsible for loading initial data into the database when the application starts.
 * It implements CommandLineRunner, which allows the execution of code at startup.
 * The primary function of this class is to load SWIFT code data from an Excel file.
 */
@Component
public class DataLoader implements CommandLineRunner {

    // The service used to manage SWIFT codes and interact with the database
    private final SwiftTableManager swiftTableManager;

    /**
     * Constructor to initialize the DataLoader with the required SwiftTableManager.
     *
     * @param swiftTableManager The SwiftTableManager to be used for loading data.
     */
    public DataLoader(SwiftTableManager swiftTableManager) {
        this.swiftTableManager = swiftTableManager;
    }

    /**
     * The run method is executed after the application context is loaded.
     * It loads SWIFT code data from an Excel file into the database.
     *
     * @param args Command-line arguments (not used in this case).
     */
    @Override
    public void run(String... args) {
        // Uncomment the line below to clear the SWIFT codes table before loading new data
        // swiftTableManager.clearSwiftCodesTable();

        // Load SWIFT code data from the provided Excel file
        swiftTableManager.loadSwiftDataFromExcel("src/main/resources/Interns_2025_SWIFT_CODES.xlsx");
    }
}
