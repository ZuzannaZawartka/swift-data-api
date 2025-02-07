package com.example.swiftdataapi;

import com.example.swiftdataapi.dto.BranchSwiftCodeDTO;
import com.example.swiftdataapi.model.SwiftCodeEntity;
import com.example.swiftdataapi.repository.SwiftCodeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class contains integration tests for the SwiftCodeController.
 * The tests check the correct behavior of the API endpoints such as adding, retrieving, and deleting SWIFT codes.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SwiftCodeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc is used for sending HTTP requests and verifying the responses

    @Autowired
    private SwiftCodeRepository swiftCodeRepository; // Repository to interact with the database

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper is used to convert Java objects to JSON and vice versa

    /**
     * Clears the repository before each test to ensure the database is clean.
     */
    @BeforeEach
    void setUp() {
        swiftCodeRepository.deleteAll(); // Clears all records from the database before each test
    }

    /**
     * Test for successfully adding a new SWIFT code.
     */
    @Test
    void testAddSwiftCode_Success() throws Exception {
        // Given: Prepare a DTO object for the new SWIFT code
        BranchSwiftCodeDTO requestDTO = new BranchSwiftCodeDTO(
                "123 Main St", "Test Bank", "US", "United States", true, "TESTUS33XXX");

        // When & Then: Perform a POST request to add the new SWIFT code and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/swift-codes/") // Send POST request
                        .contentType(MediaType.APPLICATION_JSON) // Specify that the request body is in JSON format
                        .content(objectMapper.writeValueAsString(requestDTO))) // Convert DTO to JSON and set as request body
                .andExpect(status().isOk()) // Expect status code 200 OK
                .andExpect(jsonPath("$.message").value("SWIFT code successfully added.")); // Verify the response message
    }

    /**
     * Test for retrieving SWIFT code details.
     */
    @Test
    void testGetSwiftCodeDetails_Success() throws Exception {
        // Given: Save a sample SWIFT code entity to the repository
        SwiftCodeEntity entity = new SwiftCodeEntity(
                null, "US", "TESTUS33XXX", "Test Bank", "123 Main St", "United States", true);
        swiftCodeRepository.save(entity); // Save the entity to the database

        // When & Then: Perform a GET request to retrieve the SWIFT code and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/swift-codes/TESTUS33XXX")) // Send GET request with SWIFT code as path variable
                .andExpect(status().isOk()) // Expect status code 200 OK
                .andExpect(jsonPath("$.swiftCode").value("TESTUS33XXX")); // Verify the response contains the correct SWIFT code
    }

    /**
     * Test for successfully deleting a SWIFT code.
     */
    @Test
    void testDeleteSwiftCode_Success() throws Exception {
        // Given: Save a sample SWIFT code entity to the repository
        SwiftCodeEntity entity = new SwiftCodeEntity(
                null, "US", "TESTUS33XXX", "Test Bank", "123 Main St", "United States", true);
        swiftCodeRepository.save(entity); // Save the entity to the database

        // When & Then: Perform a DELETE request to remove the SWIFT code and verify the response
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/swift-codes/TESTUS33XXX") // Send DELETE request
                        .param("bankName", "Test Bank") // Include bankName as a query parameter
                        .param("countryISO2", "US")) // Include countryISO2 as a query parameter
                .andExpect(status().isOk()) // Expect status code 200 OK
                .andExpect(jsonPath("$.message").value("SWIFT code successfully deleted.")); // Verify the response message
    }
}
