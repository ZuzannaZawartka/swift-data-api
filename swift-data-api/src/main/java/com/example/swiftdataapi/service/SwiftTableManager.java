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

    public void loadSwiftDataFromExcel(String filePath) {
        List<SwiftCodeEntity> swiftCodeEntities = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath))) {
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();  // Pomiń nagłówek

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Pobierz wartości z wiersza
                String countryISO2 = getCellValue(row, 0).toUpperCase();
                String swiftCode = getCellValue(row, 1);
                String bankName = getCellValue(row, 3);
                String address = getCellValue(row, 4);
                String countryName = getCellValue(row, 6).toUpperCase();

                // Sprawdzenie, czy to jest główna siedziba
                boolean isHeadquarter = swiftCode.endsWith("XXX");

                // Sprawdź, czy identyczny rekord już istnieje w bazie
                boolean exists = swiftCodeRepository.findBySwiftCodeAndBankNameAndCountryISO2(swiftCode, bankName, countryISO2).isPresent();

                if (!exists) {
                    // Utwórz obiekt encji z nowymi danymi tylko jeśli rekord nie istnieje
                    SwiftCodeEntity entity = new SwiftCodeEntity(
                            null,  // ID - generowane automatycznie
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

            // Zapisz nowe dane do bazy danych
            if (!swiftCodeEntities.isEmpty()) {
                swiftCodeRepository.saveAll(swiftCodeEntities);
                System.out.println("Nowe dane SWIFT zostały pomyślnie załadowane do bazy danych.");
            } else {
                System.out.println("Nie znaleziono nowych danych do załadowania.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Błąd podczas ładowania danych SWIFT: " + e.getMessage(), e);
        }
    }



    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return cell != null ? cell.toString().trim() : "";
    }

    public List<SwiftCodeEntity> getAllSwiftCodes() {
        return swiftCodeRepository.findAll();
    }

    public Object getSwiftCodeDetails(String swiftCode) {
        // Znajdź główną encję (headquarter lub branch)
        Optional<SwiftCodeEntity> mainEntityOpt = swiftCodeRepository.findBySwiftCode(swiftCode);

        if (mainEntityOpt.isEmpty()) {
            // Jeśli kod SWIFT nie istnieje, rzucamy wyjątek z informacją
            throw new IllegalArgumentException("SWIFT code not found: " + swiftCode);
        }

        SwiftCodeEntity mainEntity = mainEntityOpt.get();

        if (mainEntity.getHeadquarter()) {
            List<BranchListDTO> branches = swiftCodeRepository.findAll().stream()
                    .filter(entity -> entity.getSwiftCode().startsWith(mainEntity.getSwiftCode().substring(0, 8))
                            && !entity.getSwiftCode().equals(mainEntity.getSwiftCode()))  // Ensure the headquarter is excluded
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
        }
        else {
            // Zwróć DTO dla zwykłego oddziału bez branches
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


    public CountrySwiftCodesResponseDTO getSwiftCodesForCountry(String countryISO2code) {
        List<SwiftCodeEntity> entities = swiftCodeRepository.findAllByCountryISO2(countryISO2code.toUpperCase());

        if (entities.isEmpty()) {
            throw new IllegalArgumentException("No SWIFT codes found for country ISO2 code: " + countryISO2code);
        }

        String countryName = entities.get(0).getCountryName(); // Pobieramy nazwę kraju

        List<BranchListDTO> swiftCodes = entities.stream()
                .map(entity -> new BranchListDTO(
                        entity.getAddress(),
                        entity.getBankName(),
                        entity.getCountryISO2(),
                        entity.getHeadquarter(),
                        entity.getSwiftCode()
                ))
                .collect(Collectors.toList());

        // Zwracamy CountrySwiftCodesResponseDTO, który ma strukturę zgodną z JSON-em
        return new CountrySwiftCodesResponseDTO(countryISO2code.toUpperCase(), countryName, swiftCodes);
    }

    public SwiftCodeEntity addSwiftCode(BranchSwiftCodeDTO requestDTO) {

        System.out.println("Request DTO: " + requestDTO);
        System.out.println("Is Headquarter: " + requestDTO.getHeadquarter());

        // Sprawdzenie, czy wpis już istnieje
        boolean exists = swiftCodeRepository.findBySwiftCodeAndBankNameAndCountryISO2(
                requestDTO.getSwiftCode(),
                requestDTO.getBankName(),
                requestDTO.getCountryISO2()
        ).isPresent();

        if (exists) {
            throw new IllegalArgumentException("SWIFT code already exists for this bank in this country." + requestDTO.getHeadquarter());
        }

        if (requestDTO.getSwiftCode().endsWith("XXX")) {
            // If it's marked as a branch (headquarter = false), it's invalid
            if (!requestDTO.getHeadquarter()) {
                throw new IllegalArgumentException("A SWIFT code  a jest  "+ requestDTO.getHeadquarter()+" ending with 'XXX' must be a headquarter, not a branch.");
            }
        } else {
            // Case 2: If the SWIFT code does not end with 'XXX', it must **not** be a headquarter
            if (requestDTO.getHeadquarter()) {
                throw new IllegalArgumentException("SWIFT code "+ requestDTO.getHeadquarter()+" for headquarter must end with 'XXX'.");
            }
        }

        // For branch, check if the first 8 characters match a headquarter
        if (!requestDTO.getHeadquarter()) {
            String headquarterPrefix = requestDTO.getSwiftCode().substring(0, 8);
            boolean hasMatchingHeadquarter = swiftCodeRepository.findBySwiftCode(headquarterPrefix + "XXX").isPresent();

            if (!hasMatchingHeadquarter) {
                throw new IllegalArgumentException("Branch SWIFT code must match a valid headquarter's SWIFT code.");
            }
        }

        // Tworzenie nowego obiektu encji
        SwiftCodeEntity newEntry = new SwiftCodeEntity(
                null,
                requestDTO.getCountryISO2(),
                requestDTO.getSwiftCode(),
                requestDTO.getBankName(),
                requestDTO.getAddress(),
                requestDTO.getCountryName(),
                requestDTO.getHeadquarter()
        );

        // Zapis do bazy
        return swiftCodeRepository.save(newEntry);
    }


    public String deleteSwiftCode(String swiftCode, String bankName, String countryISO2) {
        // Sprawdzanie, czy rekord istnieje w bazie danych
        SwiftCodeEntity swiftCodeEntity = swiftCodeRepository.findBySwiftCodeAndBankNameAndCountryISO2(
                swiftCode, bankName, countryISO2
        ).orElseThrow(() -> new IllegalArgumentException("SWIFT code not found for the given bank and country"));

        // Sprawdzanie, czy kod SWIFT należy do głównej siedziby
        if (swiftCodeEntity.getHeadquarter()) {
            // Sprawdzanie, czy istnieją pododdziały (branches)
            boolean hasBranches = swiftCodeRepository.findAll().stream()
                    .anyMatch(entity -> entity.getSwiftCode().startsWith(swiftCode.substring(0, 8))
                            && !entity.getSwiftCode().equals(swiftCode));

            if (hasBranches) {
                throw new IllegalArgumentException("Cannot delete this SWIFT code as it has associated branches.");
            }
        }

        // Usuwanie rekordu z bazy
        swiftCodeRepository.delete(swiftCodeEntity);

        return "SWIFT code successfully deleted.";
    }

    public void clearSwiftCodesTable() {
        swiftCodeRepository.deleteAll();
        System.out.println("Tabela swift_codes została wyczyszczona.");
    }

}
