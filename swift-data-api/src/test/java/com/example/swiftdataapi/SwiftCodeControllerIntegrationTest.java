package com.example.swiftdataapi;

import com.example.swiftdataapi.dto.BranchSwiftCodeDTO;
import com.example.swiftdataapi.model.SwiftCodeEntity;
import com.example.swiftdataapi.repository.SwiftCodeRepository;
import com.example.swiftdataapi.service.SwiftTableManager;
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

@SpringBootTest
@AutoConfigureMockMvc
class SwiftCodeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        swiftCodeRepository.deleteAll(); // Czyszczenie bazy danych przed każdym testem
    }

    @Test
    void testAddSwiftCode_Success() throws Exception {
        // Given
        BranchSwiftCodeDTO requestDTO = new BranchSwiftCodeDTO(
                "123 Main St", "Test Bank", "US", "United States", true, "TESTUS33XXX");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/swift-codes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code successfully added."));
    }

    @Test
    void testGetSwiftCodeDetails_Success() throws Exception {
        // Given
        SwiftCodeEntity entity = new SwiftCodeEntity(
                null, "US", "TESTUS33XXX", "Test Bank", "123 Main St", "United States", true);
        swiftCodeRepository.save(entity);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/swift-codes/TESTUS33XXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("TESTUS33XXX"));
    }

    @Test
    void testDeleteSwiftCode_Success() throws Exception {
        // Given
        SwiftCodeEntity entity = new SwiftCodeEntity(
                null, "US", "TESTUS33XXX", "Test Bank", "123 Main St", "United States", true);
        swiftCodeRepository.save(entity);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/swift-codes/TESTUS33XXX")
                        .param("bankName", "Test Bank")
                        .param("countryISO2", "US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code successfully deleted."));
    }

}