package com.example.swiftdataapi;

import com.example.swiftdataapi.controller.SwiftCodeController;
import com.example.swiftdataapi.dto.BranchSwiftCodeDTO;
import com.example.swiftdataapi.service.SwiftTableManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SwiftCodeController.
 * These tests validate the correct behavior of the controller's endpoints
 * by mocking the service layer (SwiftTableManager).
 */
class SwiftCodeControllerTest {

    @Mock
    private SwiftTableManager swiftTableManager; // Mocking the service that handles the logic for SWIFT codes.

    @InjectMocks
    private SwiftCodeController swiftCodeController; // The controller being tested. Mocks will be injected into this class.

    /**
     * Sets up the mocks before each test to initialize the mock objects.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks before each test method runs.
    }

    /**
     * Test for successfully loading SWIFT data from Excel.
     */
    @Test
    void testLoadExcel() {
        // Given: Mock the loadSwiftDataFromExcel method to do nothing when invoked
        doNothing().when(swiftTableManager).loadSwiftDataFromExcel(anyString());

        // When: Invoke the controller's method to load Excel data
        ResponseEntity<String> response = swiftCodeController.loadExcel();

        // Then: Validate that the response is OK and the correct message is returned
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Data has been loaded.", response.getBody());

        // Verify that the service's method was called exactly once
        verify(swiftTableManager, times(1)).loadSwiftDataFromExcel(anyString());
    }

    /**
     * Test for successfully retrieving SWIFT code details.
     */
    @Test
    void testGetSwiftCodeDetails_Success() {
        // Given: Mock the getSwiftCodeDetails method to return a map with the correct swift code
        String swiftCode = "TESTUS33XXX";
        when(swiftTableManager.getSwiftCodeDetails(swiftCode)).thenReturn(Map.of("swiftCode", swiftCode));

        // When: Invoke the controller's method to get SWIFT code details
        ResponseEntity<Object> response = swiftCodeController.getSwiftCodeDetails(swiftCode);

        // Then: Validate the response status and that the correct SWIFT code is in the response body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(swiftCode, ((Map<?, ?>) response.getBody()).get("swiftCode"));
    }

    /**
     * Test for retrieving SWIFT code details when the SWIFT code is not found.
     */
    @Test
    void testGetSwiftCodeDetails_NotFound() {
        // Given: Mock the getSwiftCodeDetails method to throw an exception when an invalid code is provided
        String swiftCode = "INVALID";
        when(swiftTableManager.getSwiftCodeDetails(swiftCode)).thenThrow(new IllegalArgumentException("SWIFT code not found"));

        // When: Invoke the controller's method to get details for an invalid SWIFT code
        ResponseEntity<Object> response = swiftCodeController.getSwiftCodeDetails(swiftCode);

        // Then: Validate that the response status is NOT_FOUND and the correct error message is returned
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("SWIFT code not found", ((Map<?, ?>) response.getBody()).get("message"));
    }

    /**
     * Test for successfully adding a new SWIFT code.
     */
    @Test
    void testAddSwiftCode_Success() {
        // Given: Create a valid request DTO and mock the addSwiftCode method to return null (indicating success)
        BranchSwiftCodeDTO requestDTO = new BranchSwiftCodeDTO(
                "123 Main St", "Test Bank", "US", "United States", true, "TESTUS33XXX");
        when(swiftTableManager.addSwiftCode(any())).thenReturn(null);

        // When: Invoke the controller's method to add a new SWIFT code
        ResponseEntity<Map<String, String>> response = swiftCodeController.addSwiftCode(requestDTO);

        // Then: Validate that the response status is OK and that the success message is returned
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SWIFT code successfully added.", response.getBody().get("message"));
    }

    /**
     * Test for adding a SWIFT code with an invalid format.
     */
    @Test
    void testAddSwiftCode_Invalid() {
        // Given: Create a request DTO with an invalid SWIFT code format and mock the addSwiftCode method to throw an exception
        BranchSwiftCodeDTO requestDTO = new BranchSwiftCodeDTO(
                "123 Main St", "Test Bank", "US", "United States", true, "INVALID");
        when(swiftTableManager.addSwiftCode(any())).thenThrow(new IllegalArgumentException("Invalid SWIFT code format. It must end with 'XXX'."));

        // When: Invoke the controller's method to add an invalid SWIFT code
        ResponseEntity<Map<String, String>> response = swiftCodeController.addSwiftCode(requestDTO);

        // Then: Validate that the response status is BAD_REQUEST and the correct error message is returned
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid SWIFT code format. It must end with 'XXX'.", response.getBody().get("message"));
    }

    /**
     * Test for successfully deleting a SWIFT code.
     */
    @Test
    void testDeleteSwiftCode_Success() {
        // Given: Mock the deleteSwiftCode method to return a success message
        String swiftCode = "TESTUS33XXX";
        String bankName = "Test Bank";
        String countryISO2 = "US";
        when(swiftTableManager.deleteSwiftCode(swiftCode, bankName, countryISO2)).thenReturn("SWIFT code deleted successfully.");

        // When: Invoke the controller's method to delete the SWIFT code
        ResponseEntity<Map<String, String>> response = swiftCodeController.deleteSwiftCode(swiftCode, bankName, countryISO2);

        // Then: Validate that the response status is OK and the correct success message is returned
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SWIFT code deleted successfully.", response.getBody().get("message"));
    }

    /**
     * Test for attempting to delete a SWIFT code that does not exist.
     */
    @Test
    void testDeleteSwiftCode_NotFound() {
        // Given: Mock the deleteSwiftCode method to throw an exception when an invalid SWIFT code is provided
        String swiftCode = "INVALID";
        String bankName = "Test Bank";
        String countryISO2 = "US";
        when(swiftTableManager.deleteSwiftCode(swiftCode, bankName, countryISO2)).thenThrow(new IllegalArgumentException("SWIFT code not found."));

        // When: Invoke the controller's method to delete a non-existent SWIFT code
        ResponseEntity<Map<String, String>> response = swiftCodeController.deleteSwiftCode(swiftCode, bankName, countryISO2);

        // Then: Validate that the response status is NOT_FOUND and the correct error message is returned
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("SWIFT code not found.", response.getBody().get("message"));
    }
}
