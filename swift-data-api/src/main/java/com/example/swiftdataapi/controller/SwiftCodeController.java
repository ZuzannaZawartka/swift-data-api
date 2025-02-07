package com.example.swiftdataapi.controller;

import com.example.swiftdataapi.dto.BranchSwiftCodeDTO;
import com.example.swiftdataapi.model.SwiftCodeEntity;
import com.example.swiftdataapi.service.SwiftTableManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController {

    private final SwiftTableManager swiftTableManager;

    // Constructor that injects the SwiftTableManager service for handling business logic
    public SwiftCodeController(SwiftTableManager swiftTableManager) {
        this.swiftTableManager = swiftTableManager;
    }

    /**
     * Endpoint to load SWIFT codes data from an Excel file into the database.
     *
     * @return Response indicating whether the data load was successful or not.
     */
    @GetMapping("/load-excel")
    public ResponseEntity<String> loadExcel() {
        // Trigger the service to load data from a pre-defined Excel file
        swiftTableManager.loadSwiftDataFromExcel("src/main/resources/Interns_2025_SWIFT_CODES.xlsx");
        // Return a simple confirmation response
        return ResponseEntity.ok("Data has been loaded.");
    }

    /**
     * Endpoint to retrieve all SWIFT codes for a given country based on its ISO2 code.
     *
     * @param countryISO2code The 2-letter ISO code of the country.
     * @return A list of SWIFT codes associated with the given country.
     */
    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<Object> getSwiftCodeForCountry(@PathVariable("countryISO2code") String countryISO2code) {
        // Fetch and return all SWIFT codes for the specified country
        return ResponseEntity.ok(swiftTableManager.getSwiftCodesForCountry(countryISO2code));
    }

    /**
     * Endpoint to retrieve detailed information about a specific SWIFT code.
     *
     * @param swiftCode The SWIFT code to fetch details for.
     * @return Details of the requested SWIFT code if found, or a 'not found' response.
     */
    @GetMapping("/{swiftCode}")
    public ResponseEntity<Object> getSwiftCodeDetails(@PathVariable("swiftCode") String swiftCode) {
        try {
            // Retrieve and return details for the specified SWIFT code
            return ResponseEntity.ok(swiftTableManager.getSwiftCodeDetails(swiftCode));
        } catch (IllegalArgumentException e) {
            // If the SWIFT code is invalid or not found, return a 404 Not Found with the error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Endpoint to add a new SWIFT code entry into the system.
     *
     * @param requestDTO The data transfer object containing the details of the new SWIFT code.
     * @return A response indicating success or failure of the operation.
     */
    @PostMapping("/")
    public ResponseEntity<Map<String, String>> addSwiftCode(@RequestBody BranchSwiftCodeDTO requestDTO) {
        Map<String, String> response = new HashMap<>();

        try {
            // Add the SWIFT code using the provided data
            SwiftCodeEntity savedEntity = swiftTableManager.addSwiftCode(requestDTO);

            // Return a success message after adding the SWIFT code
            response.put("message", "SWIFT code successfully added.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // If there is an issue (e.g., SWIFT code not ending with 'XXX'), return a Bad Request response with the error
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Endpoint to delete a specific SWIFT code from the system.
     *
     * @param swiftCode The SWIFT code to be deleted.
     * @param bankName The name of the bank associated with the SWIFT code.
     * @param countryISO2 The 2-letter ISO code of the country associated with the SWIFT code.
     * @return A response indicating the success or failure of the deletion process.
     */
    @DeleteMapping("/{swift-code}")
    public ResponseEntity<Map<String, String>> deleteSwiftCode(@PathVariable("swift-code") String swiftCode,
                                                               @RequestParam("bankName") String bankName,
                                                               @RequestParam("countryISO2") String countryISO2) {
        try {
            // Delete the SWIFT code based on the provided details (swiftCode, bankName, countryISO2)
            String message = swiftTableManager.deleteSwiftCode(swiftCode, bankName, countryISO2);

            // Return a success message confirming the deletion
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            // If the deletion fails (e.g., SWIFT code not found), return a 404 Not Found response with the error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

}
