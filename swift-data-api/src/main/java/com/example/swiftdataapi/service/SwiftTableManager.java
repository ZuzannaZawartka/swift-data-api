package com.example.swiftdataapi.service;

import com.example.swiftdataapi.model.SwiftCodeEntity;
import com.example.swiftdataapi.repository.SwiftCodeRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

//                System.out.println("BankName : " + bankName);
//                // Utwórz obiekt encji z nowymi danymi
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
//
//
                swiftCodeEntities.add(entity);
            }
//
//            // Zapisz dane do bazy danych
            swiftCodeRepository.saveAll(swiftCodeEntities);
            System.out.println("Dane SWIFT zostały pomyślnie załadowane do bazy danych.");

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
}
