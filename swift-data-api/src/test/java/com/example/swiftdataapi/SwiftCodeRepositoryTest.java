package com.example.swiftdataapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.swiftdataapi.model.SwiftCodeEntity;
import com.example.swiftdataapi.repository.SwiftCodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test class for verifying the functionality of the SwiftCodeRepository.
 * This tests the interaction with the database using the repository.
 * Tests include creating, saving, and finding a SwiftCodeEntity.
 */
@SpringBootTest
public class SwiftCodeRepositoryTest {

    // Inject the SwiftCodeRepository using Spring's dependency injection
    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    /**
     * Test method to verify the creation and retrieval of a SWIFT code entity.
     * This test checks that a SwiftCodeEntity can be saved to the database
     * and retrieved correctly by its ID.
     */
    @Test
    public void testCreateAndFindSwiftCode() {
        // Given: Create a new SwiftCodeEntity with sample data
        SwiftCodeEntity entity = new SwiftCodeEntity(
                null, "PL", "CITIPLPXXXX", "Bank Handlowy", "Senatorska 16", "Poland", true);

        // When: Save the entity to the database using the repository
        SwiftCodeEntity savedEntity = swiftCodeRepository.save(entity);

        // Then: Verify that the entity was saved correctly
        // Ensure that the saved entity is not null and that it has an ID
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());

        // When: Retrieve the saved entity from the database by its ID
        SwiftCodeEntity foundEntity = swiftCodeRepository.findById(savedEntity.getId()).orElse(null);

        // Then: Verify that the retrieved entity is not null and matches the saved entity
        // Check that the SWIFT code in the found entity matches the one in the saved entity
        assertNotNull(foundEntity);
        assertEquals(savedEntity.getSwiftCode(), foundEntity.getSwiftCode());
    }
}
