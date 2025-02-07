package com.example.swiftdataapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.swiftdataapi.model.SwiftCodeEntity;
import com.example.swiftdataapi.repository.SwiftCodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SwiftCodeRepositoryTest {

    // Wstrzykiwanie repozytorium przez Spring
    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @Test
    public void testCreateAndFindSwiftCode() {
        // Przygotowanie danych testowych
        SwiftCodeEntity entity = new SwiftCodeEntity(
                null, "PL", "CITIPLPXXXX", "Bank Handlowy", "Senatorska 16", "Poland", true);

        // Zapis do bazy
        SwiftCodeEntity savedEntity = swiftCodeRepository.save(entity);

        // Weryfikacja, czy zapis się powiódł
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());

        // Znalezienie rekordu po ID
        SwiftCodeEntity foundEntity = swiftCodeRepository.findById(savedEntity.getId()).orElse(null);

        // Sprawdzamy, czy rekord został poprawnie znaleziony
        assertNotNull(foundEntity);
        assertEquals(savedEntity.getSwiftCode(), foundEntity.getSwiftCode());
    }
}
