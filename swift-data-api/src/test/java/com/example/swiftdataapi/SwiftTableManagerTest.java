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

class SwiftTableManagerTest {

    @Mock
    private SwiftCodeRepository swiftCodeRepository;

    @InjectMocks
    private SwiftTableManager swiftTableManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSwiftCode_Success() {
        BranchSwiftCodeDTO requestDTO = new BranchSwiftCodeDTO(
                "123 Main St", "Test Bank", "US", "United States", true, "TESTUS33XXX");

        SwiftCodeEntity savedEntity = new SwiftCodeEntity();
        savedEntity.setSwiftCode(requestDTO.getSwiftCode());

        when(swiftCodeRepository.save(any())).thenReturn(savedEntity);

        SwiftCodeEntity result = swiftTableManager.addSwiftCode(requestDTO);
        assertNotNull(result);
        assertEquals("TESTUS33XXX", result.getSwiftCode());
    }


    @Test
    void testAddSwiftCode_InvalidSwift() {
        BranchSwiftCodeDTO requestDTO = new BranchSwiftCodeDTO(
                "123 Main St", "Test Bank", "US", "United States", true, "TESTUS33");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            swiftTableManager.addSwiftCode(requestDTO);
        });

        // Dostosowany komunikat błędu
        assertEquals("SWIFT code true for headquarter must end with 'XXX'.", exception.getMessage());
    }

    @Test
    void testDeleteSwiftCode_NotFound() {
        when(swiftCodeRepository.findBySwiftCodeAndBankNameAndCountryISO2(any(), any(), any()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            swiftTableManager.deleteSwiftCode("TESTUS33XXX", "Test Bank", "US");
        });

        // Nowy komunikat błędu pasujący do rzeczywistego wyniku
        assertEquals("SWIFT code not found for the given bank and country", exception.getMessage());
    }

    @Test
    void testDeleteSwiftCode_Success() {
        String swiftCode = "TESTUS33XXX";
        String bankName = "Test Bank";
        String countryISO2 = "US";

        SwiftCodeEntity entity = new SwiftCodeEntity();
        entity.setSwiftCode(swiftCode);

        when(swiftCodeRepository.findBySwiftCodeAndBankNameAndCountryISO2(swiftCode, bankName, countryISO2))
                .thenReturn(Optional.of(entity));

        String result = swiftTableManager.deleteSwiftCode(swiftCode, bankName, countryISO2);

        // Nowa wersja komunikatu
        assertEquals("SWIFT code successfully deleted.", result);

        verify(swiftCodeRepository, times(1)).delete(entity);
    }

}
