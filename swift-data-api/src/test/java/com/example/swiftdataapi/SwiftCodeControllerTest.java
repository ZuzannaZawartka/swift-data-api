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

class SwiftCodeControllerTest {

    @Mock
    private SwiftTableManager swiftTableManager;

    @InjectMocks
    private SwiftCodeController swiftCodeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadExcel() {
        // Given
        doNothing().when(swiftTableManager).loadSwiftDataFromExcel(anyString());

        // When
        ResponseEntity<String> response = swiftCodeController.loadExcel();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Dane zostały załadowane.", response.getBody());
        verify(swiftTableManager, times(1)).loadSwiftDataFromExcel(anyString());
    }

    @Test
    void testGetSwiftCodeDetails_Success() {
        // Given
        String swiftCode = "TESTUS33XXX";
        when(swiftTableManager.getSwiftCodeDetails(swiftCode)).thenReturn(Map.of("swiftCode", swiftCode));

        // When
        ResponseEntity<Object> response = swiftCodeController.getSwiftCodeDetails(swiftCode);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(swiftCode, ((Map<?, ?>) response.getBody()).get("swiftCode"));
    }

    @Test
    void testGetSwiftCodeDetails_NotFound() {
        // Given
        String swiftCode = "INVALID";
        when(swiftTableManager.getSwiftCodeDetails(swiftCode)).thenThrow(new IllegalArgumentException("SWIFT code not found"));

        // When
        ResponseEntity<Object> response = swiftCodeController.getSwiftCodeDetails(swiftCode);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("SWIFT code not found", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void testAddSwiftCode_Success() {
        // Given
        BranchSwiftCodeDTO requestDTO = new BranchSwiftCodeDTO(
                "123 Main St", "Test Bank", "US", "United States", true, "TESTUS33XXX");
        when(swiftTableManager.addSwiftCode(any())).thenReturn(null);

        // When
        ResponseEntity<Map<String, String>> response = swiftCodeController.addSwiftCode(requestDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SWIFT code successfully added.", response.getBody().get("message"));
    }

    @Test
    void testAddSwiftCode_Invalid() {
        // Given
        BranchSwiftCodeDTO requestDTO = new BranchSwiftCodeDTO(
                "123 Main St", "Test Bank", "US", "United States", true, "INVALID");
        when(swiftTableManager.addSwiftCode(any())).thenThrow(new IllegalArgumentException("Invalid SWIFT code format. It must end with 'XXX'."));

        // When
        ResponseEntity<Map<String, String>> response = swiftCodeController.addSwiftCode(requestDTO);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid SWIFT code format. It must end with 'XXX'.", response.getBody().get("message"));
    }

    @Test
    void testDeleteSwiftCode_Success() {
        // Given
        String swiftCode = "TESTUS33XXX";
        String bankName = "Test Bank";
        String countryISO2 = "US";
        when(swiftTableManager.deleteSwiftCode(swiftCode, bankName, countryISO2)).thenReturn("SWIFT code deleted successfully.");

        // When
        ResponseEntity<Map<String, String>> response = swiftCodeController.deleteSwiftCode(swiftCode, bankName, countryISO2);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SWIFT code deleted successfully.", response.getBody().get("message"));
    }

    @Test
    void testDeleteSwiftCode_NotFound() {
        // Given
        String swiftCode = "INVALID";
        String bankName = "Test Bank";
        String countryISO2 = "US";
        when(swiftTableManager.deleteSwiftCode(swiftCode, bankName, countryISO2)).thenThrow(new IllegalArgumentException("SWIFT code not found."));

        // When
        ResponseEntity<Map<String, String>> response = swiftCodeController.deleteSwiftCode(swiftCode, bankName, countryISO2);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("SWIFT code not found.", response.getBody().get("message"));
    }
}