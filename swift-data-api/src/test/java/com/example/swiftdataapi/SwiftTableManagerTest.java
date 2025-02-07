package com.example.swiftdataapi;

import com.example.swiftdataapi.dto.BranchSwiftCodeDTO;
import com.example.swiftdataapi.model.SwiftCodeEntity;
import com.example.swiftdataapi.repository.SwiftCodeRepository;
import com.example.swiftdataapi.service.SwiftTableManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for the SwiftTableManager class.
 * This test class mocks the SwiftCodeRepository and verifies the logic in SwiftTableManager
 * for adding and deleting SWIFT codes.
 */
class SwiftTableManagerTest {

    // Mock the SwiftCodeRepository to simulate database operations
    @Mock
    private SwiftCodeRepository swiftCodeRepository;

    // Inject the mock repository into the SwiftTableManager instance
    @InjectMocks
    private SwiftTableManager swiftTableManager;

    /**
     * Setup method to initialize mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mock objects before each test
    }

    /**
     * Test case to validate the successful addition of a SWIFT code.
     */
    @Test
    void testAddSwiftCode_Success() {
        // Given: Create a valid BranchSwiftCodeDTO with sample data
        BranchSwiftCodeDTO requestDTO = new BranchSwiftCodeDTO(
                "123 Main St", "Test Bank", "US", "United States", true, "TESTUS33XXX");

        // Given: Create a mock saved entity to be returned by the repository
        SwiftCodeEntity savedEntity = new SwiftCodeEntity();
        savedEntity.setSwiftCode(requestDTO.getSwiftCode());

        // When: Mock the repository save method to return the saved entity
        when(swiftCodeRepository.save(any())).thenReturn(savedEntity);

        // When: Call the addSwiftCode method
        SwiftCodeEntity result = swiftTableManager.addSwiftCode(requestDTO);

        // Then: Verify that the returned entity is not null and the SWIFT code matches
        assertNotNull(result);
        assertEquals("TESTUS33XXX", result.getSwiftCode());
    }

    /**
     * Test case to handle invalid SWIFT code format when adding a SWIFT code.
     */
    @Test
    void testAddSwiftCode_InvalidSwift() {
        // Given: Create a BranchSwiftCodeDTO with an invalid SWIFT code (doesn't end with 'XXX')
        BranchSwiftCodeDTO requestDTO = new BranchSwiftCodeDTO(
                "123 Main St", "Test Bank", "US", "United States", true, "TESTUS33");

        // When & Then: Assert that an IllegalArgumentException is thrown with the appropriate error message
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            swiftTableManager.addSwiftCode(requestDTO);
        });

        assertEquals("SWIFT code for headquarter must end with 'XXX'.", exception.getMessage());
    }

    /**
     * Test case to handle the scenario where the SWIFT code to delete is not found.
     */
    @Test
    void testDeleteSwiftCode_NotFound() {
        // Given: Mock the repository to return an empty Optional (SWIFT code not found)
        when(swiftCodeRepository.findBySwiftCodeAndBankNameAndCountryISO2(any(), any(), any()))
                .thenReturn(Optional.empty());

        // When & Then: Assert that an IllegalArgumentException is thrown with the appropriate error message
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            swiftTableManager.deleteSwiftCode("TESTUS33XXX", "Test Bank", "US");
        });

        assertEquals("SWIFT code not found for the given bank and country", exception.getMessage());
    }

    /**
     * Test case to validate the successful deletion of a SWIFT code.
     */
    @Test
    void testDeleteSwiftCode_Success() {
        // Given: Sample data for SWIFT code, bank name, and country
        String swiftCode = "TESTUS33XXX";
        String bankName = "Test Bank";
        String countryISO2 = "US";

        // Given: Create a SwiftCodeEntity to simulate an existing entity in the repository
        SwiftCodeEntity entity = new SwiftCodeEntity();
        entity.setSwiftCode(swiftCode);

        // When: Mock the repository to return the entity when searching for the SWIFT code
        when(swiftCodeRepository.findBySwiftCodeAndBankNameAndCountryISO2(swiftCode, bankName, countryISO2))
                .thenReturn(Optional.of(entity));

        // When: Call the deleteSwiftCode method to delete the entity
        String result = swiftTableManager.deleteSwiftCode(swiftCode, bankName, countryISO2);

        // Then: Verify that the correct success message is returned
        assertEquals("SWIFT code successfully deleted.", result);

        // Then: Verify that the repository's delete method was called once
        verify(swiftCodeRepository, times(1)).delete(entity);
    }
}
