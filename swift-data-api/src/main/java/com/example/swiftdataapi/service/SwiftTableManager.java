package com.example.swiftdataapi.service;

import com.example.swiftdataapi.dto.BranchListDTO;
import com.example.swiftdataapi.dto.BranchSwiftCodeDTO;
import com.example.swiftdataapi.dto.SwiftCodeResponseDTO;
import com.example.swiftdataapi.dto.endpoint2.CountrySwiftCodesResponseDTO;
import com.example.swiftdataapi.model.SwiftCodeEntity;
import com.example.swiftdataapi.repository.SwiftCodeRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SwiftTableManager {

    private final SwiftCodeRepository swiftCodeRepository;

    public SwiftTableManager(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }

    /**
     * Loads SWIFT data from an Excel file and stores it in the database.
     * @param filePath Path to the Excel file
     */
    public void loadSwiftDataFromExcel(String filePath) {
        List<SwiftCodeEntity> swiftCodeEntities = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath))) {
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();  // Skip header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Extract values from each row
                String countryISO2 = getCellValue(row, 0).toUpperCase();
                String swiftCode = getCellValue(row, 1);
                String bankName = getCellValue(row, 3);
                String address = getCellValue(row, 4);
                String countryName = getCellValue(row, 6).toUpperCase();

                // Check if this is a headquarter
                boolean isHeadquarter = swiftCode.endsWith("XXX");

                // Check if the record already exists
                boolean exists = swiftCodeRepository.findBySwiftCodeAndBankNameAndCountryISO2(swiftCode, bankName, countryISO2).isPresent();

                if (!exists) {
                    SwiftCodeEntity entity = new SwiftCodeEntity(
                            null,  // ID is generated automatically
                            countryISO2,
                            swiftCode,
                            bankName,
                            address,
                            countryName,
                            isHeadquarter
                    );
                    swiftCodeEntities.add(entity);
                }
            }

            // Save new records to the database
            if (!swiftCodeEntities.isEmpty()) {
                swiftCodeRepository.saveAll(swiftCodeEntities);
                System.out.println("New SWIFT data has been successfully loaded into the database.");
            } else {
                System.out.println("No new data to load.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while loading SWIFT data: " + e.getMessage(), e);
        }
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return cell != null ? cell.toString().trim() : "";
    }

    /**
     * Retrieves all SWIFT codes from the database.
     * @return List of all SwiftCodeEntity records
     */
    public List<SwiftCodeEntity> getAllSwiftCodes() {
        return swiftCodeRepository.findAll();
    }

    /**
     * Retrieves SWIFT code details including branches if it's a headquarter.
     * @param swiftCode The SWIFT code to retrieve
     * @return SwiftCodeResponseDTO for headquarter or BranchSwiftCodeDTO for a branch
     */
    public Object getSwiftCodeDetails(String swiftCode) {
        Optional<SwiftCodeEntity> mainEntityOpt = swiftCodeRepository.findBySwiftCode(swiftCode);

        if (mainEntityOpt.isEmpty()) {
            throw new IllegalArgumentException("SWIFT code not found: " + swiftCode);
        }

        SwiftCodeEntity mainEntity = mainEntityOpt.get();

        if (mainEntity.getHeadquarter()) {
            // Get branches for headquarter
            List<BranchListDTO> branches = swiftCodeRepository.findAll().stream()
                    .filter(entity -> entity.getSwiftCode().startsWith(mainEntity.getSwiftCode().substring(0, 8)) &&
                            !entity.getSwiftCode().equals(mainEntity.getSwiftCode()))  // Exclude the main entity
                    .map(entity -> new BranchListDTO(
                            entity.getAddress(),
                            entity.getBankName(),
                            entity.getCountryISO2(),
                            entity.getHeadquarter(),
                            entity.getSwiftCode()
                    ))
                    .collect(Collectors.toList());

            return new SwiftCodeResponseDTO(
                    mainEntity.getAddress(),
                    mainEntity.getBankName(),
                    mainEntity.getCountryISO2(),
                    mainEntity.getCountryName(),
                    mainEntity.getHeadquarter(),
                    mainEntity.getSwiftCode(),
                    branches
            );
        } else {
            // Return branch information if it's not a headquarter
            return new BranchSwiftCodeDTO(
                    mainEntity.getAddress(),
                    mainEntity.getBankName(),
                    mainEntity.getCountryISO2(),
                    mainEntity.getCountryName(),
                    mainEntity.getHeadquarter(),
                    mainEntity.getSwiftCode()
            );
        }
    }

    /**
     * Retrieves SWIFT codes for a specific country.
     * @param countryISO2code Country ISO2 code
     * @return CountrySwiftCodesResponseDTO containing the country's SWIFT codes
     */
    public CountrySwiftCodesResponseDTO getSwiftCodesForCountry(String countryISO2code) {
        List<SwiftCodeEntity> entities = swiftCodeRepository.findAllByCountryISO2(countryISO2code.toUpperCase());

        if (entities.isEmpty()) {
            throw new IllegalArgumentException("No SWIFT codes found for country ISO2 code: " + countryISO2code);
        }

        String countryName = entities.get(0).getCountryName(); // Get country name from the first entity

        List<BranchListDTO> swiftCodes = entities.stream()
                .map(entity -> new BranchListDTO(
                        entity.getAddress(),
                        entity.getBankName(),
                        entity.getCountryISO2(),
                        entity.getHeadquarter(),
                        entity.getSwiftCode()
                ))
                .collect(Collectors.toList());

        return new CountrySwiftCodesResponseDTO(countryISO2code.toUpperCase(), countryName, swiftCodes);
    }

    /**
     * Adds a new SWIFT code entry.
     * @param requestDTO Data transfer object containing SWIFT code details
     * @return The newly added SwiftCodeEntity
     */
    public SwiftCodeEntity addSwiftCode(BranchSwiftCodeDTO requestDTO) {

        // Check if the SWIFT code already exists in the database
        boolean exists = swiftCodeRepository.findBySwiftCodeAndBankNameAndCountryISO2(
                requestDTO.getSwiftCode(),
                requestDTO.getBankName(),
                requestDTO.getCountryISO2()
        ).isPresent();

        if (exists) {
            throw new IllegalArgumentException("SWIFT code already exists for this bank and country.");
        }

        // Validation: If it's a headquarter, the code must end with "XXX"
        if (requestDTO.getSwiftCode().endsWith("XXX")) {
            if (!requestDTO.getHeadquarter()) {
                throw new IllegalArgumentException("SWIFT code ending with 'XXX' must be for a headquarter.");
            }
        } else {
            if (requestDTO.getHeadquarter()) {
                throw new IllegalArgumentException("SWIFT code for headquarter must end with 'XXX'.");
            }
        }

        // Validation for branch: Check if a matching headquarter exists
        if (!requestDTO.getHeadquarter()) {
            String headquarterPrefix = requestDTO.getSwiftCode().substring(0, 8);
            boolean hasMatchingHeadquarter = swiftCodeRepository.findBySwiftCode(headquarterPrefix + "XXX").isPresent();

            if (!hasMatchingHeadquarter) {
                throw new IllegalArgumentException("Branch SWIFT code must match a valid headquarter's SWIFT code.");
            }
        }

        // Create a new SwiftCodeEntity and save it to the database
        SwiftCodeEntity newEntry = new SwiftCodeEntity(
                null,
                requestDTO.getCountryISO2(),
                requestDTO.getSwiftCode(),
                requestDTO.getBankName(),
                requestDTO.getAddress(),
                requestDTO.getCountryName(),
                requestDTO.getHeadquarter()
        );

        return swiftCodeRepository.save(newEntry);
    }

    /**
     * Deletes a SWIFT code from the database.
     * @param swiftCode SWIFT code to delete
     * @param bankName Bank name associated with the SWIFT code
     * @param countryISO2 Country ISO2 code
     * @return Message indicating success or failure
     */
    public String deleteSwiftCode(String swiftCode, String bankName, String countryISO2) {
        SwiftCodeEntity swiftCodeEntity = swiftCodeRepository.findBySwiftCodeAndBankNameAndCountryISO2(
                swiftCode, bankName, countryISO2
        ).orElseThrow(() -> new IllegalArgumentException("SWIFT code not found for the given bank and country"));

        if (swiftCodeEntity.getHeadquarter()) {
            // Check if there are any associated branches
            boolean hasBranches = swiftCodeRepository.findAll().stream()
                    .anyMatch(entity -> entity.getSwiftCode().startsWith(swiftCode.substring(0, 8)) &&
                            !entity.getSwiftCode().equals(swiftCode));

            if (hasBranches) {
                throw new IllegalArgumentException("Cannot delete headquarter SWIFT code as it has associated branches.");
            }
        }

        // Delete the record
        swiftCodeRepository.delete(swiftCodeEntity);

        return "SWIFT code successfully deleted.";
    }

    /**
     * Clears all records from the swift_codes table.
     */
    public void clearSwiftCodesTable() {
        swiftCodeRepository.deleteAll();
        System.out.println("All records from swift_codes table have been cleared.");
    }
}
