package com.example.swiftdataapi.service;

import com.example.swiftdataapi.dto.BranchDTO;
import com.example.swiftdataapi.dto.BranchSwiftCodeDTO;
import com.example.swiftdataapi.dto.SwiftCodeResponseDTO;
import com.example.swiftdataapi.model.SwiftCodeEntity;
import com.example.swiftdataapi.repository.SwiftCodeRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
                String codeType = getCellValue(row, 2);
                String bankName = getCellValue(row, 3);
                String address = getCellValue(row, 4);
                String townName = getCellValue(row, 5);
                String countryName = getCellValue(row, 6).toUpperCase();
                String timeZone = getCellValue(row, 7);

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
                            codeType,
                            bankName,
                            address,
                            townName,
                            countryName,
                            timeZone,
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

    // Metoda zwraca odpowiednie DTO w zależności od tego, czy jest główną siedzibą
    public Object getSwiftCodeDetails(String swiftCode) {
        // Znajdź główną encję (headquarter lub branch)
        SwiftCodeEntity mainEntity = swiftCodeRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new IllegalArgumentException("SWIFT code not found: " + swiftCode));

        if (mainEntity.isHeadquarter()) {
            List<BranchDTO> branches = swiftCodeRepository.findAll().stream()
                    .filter(entity -> entity.getSwiftCode().startsWith(mainEntity.getSwiftCode().substring(0, 8))
                            && !entity.getSwiftCode().equals(mainEntity.getSwiftCode()))  // Wyklucz siedzibę główną
                    .map(entity -> new BranchDTO(
                            entity.getAddress(),
                            entity.getBankName(),
                            entity.getCountryISO2(),
                            entity.isHeadquarter(),
                            entity.getSwiftCode()
                    ))
                    .collect(Collectors.toList());

            return new SwiftCodeResponseDTO(
                    mainEntity.getAddress(),
                    mainEntity.getBankName(),
                    mainEntity.getCountryISO2(),
                    mainEntity.getCountryName(),
                    mainEntity.isHeadquarter(),
                    mainEntity.getSwiftCode(),
                    branches
            );

        } else {
            // Zwróć DTO dla zwykłego oddziału bez branches
            return new BranchSwiftCodeDTO(
                    mainEntity.getAddress(),
                    mainEntity.getBankName(),
                    mainEntity.getCountryISO2(),
                    mainEntity.getCountryName(),
                    mainEntity.isHeadquarter(),
                    mainEntity.getSwiftCode()
            );
        }
    }


    public void clearSwiftCodesTable() {
        swiftCodeRepository.deleteAll();
        System.out.println("Tabela swift_codes została wyczyszczona.");
    }

}
