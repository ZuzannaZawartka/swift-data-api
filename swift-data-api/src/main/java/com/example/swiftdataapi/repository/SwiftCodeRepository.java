package com.example.swiftdataapi.repository;

import com.example.swiftdataapi.model.SwiftCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link SwiftCodeEntity} data in the database.
 * This interface extends JpaRepository to leverage CRUD operations, query generation,
 * and pagination functionality provided by Spring Data JPA.
 */
@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCodeEntity, Long> {

    /**
     * Finds a {@link SwiftCodeEntity} by its SWIFT code.
     *
     * @param swiftCode The SWIFT code to search for.
     * @return An Optional containing the found {@link SwiftCodeEntity}, or empty if not found.
     */
    Optional<SwiftCodeEntity> findBySwiftCode(String swiftCode);

    /**
     * Finds a {@link SwiftCodeEntity} by its SWIFT code, bank name, and country ISO2 code.
     * This method is useful when looking for a specific branch by multiple attributes.
     *
     * @param swiftCode The SWIFT code to search for.
     * @param bankName The name of the bank to search for.
     * @param countryISO2 The 2-letter ISO country code to search for.
     * @return An Optional containing the found {@link SwiftCodeEntity}, or empty if not found.
     */
    Optional<SwiftCodeEntity> findBySwiftCodeAndBankNameAndCountryISO2(String swiftCode, String bankName, String countryISO2);

    /**
     * Finds all {@link SwiftCodeEntity} objects by the country ISO2 code.
     * This method returns all SWIFT codes in a specific country.
     *
     * @param countryISO2 The 2-letter ISO country code.
     * @return A list of {@link SwiftCodeEntity} objects for the given country ISO2 code.
     */
    List<SwiftCodeEntity> findAllByCountryISO2(String countryISO2);

}
