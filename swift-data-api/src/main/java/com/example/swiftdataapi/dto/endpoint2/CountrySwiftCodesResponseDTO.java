package com.example.swiftdataapi.dto.endpoint2;

import com.example.swiftdataapi.dto.BranchListDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Data Transfer Object (DTO) for the response that contains SWIFT codes for a country.
 *
 * This class is used to return information about a country along with its associated SWIFT codes.
 * It includes the country ISO2 code, country name, and a list of branches with their SWIFT codes.
 *
 * The {@link JsonInclude} annotation ensures that fields with null values are excluded from the JSON response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountrySwiftCodesResponseDTO {

    // ISO2 country code (e.g., "US", "PL", "GB")
    private String countryISO2;

    // Full country name (e.g., "United States", "Poland", "United Kingdom")
    private String countryName;

    // List of BranchListDTO objects, each representing a bank branch and its associated SWIFT code
    private List<BranchListDTO> swiftCodes;

    /**
     * Constructor to initialize the CountrySwiftCodesResponseDTO with the provided values.
     *
     * @param countryISO2 The 2-letter ISO code of the country (e.g., "US").
     * @param countryName The name of the country (e.g., "United States").
     * @param swiftCodes A list of branches with their associated SWIFT codes.
     */
    public CountrySwiftCodesResponseDTO(String countryISO2, String countryName, List<BranchListDTO> swiftCodes) {
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.swiftCodes = swiftCodes;
    }

    /**
     * Gets the ISO2 code of the country.
     *
     * @return The 2-letter ISO code of the country.
     */
    public String getCountryISO2() {
        return countryISO2;
    }

    /**
     * Gets the name of the country.
     *
     * @return The full name of the country.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Gets the list of SWIFT codes associated with the branches in the country.
     *
     * @return A list of {@link BranchListDTO} objects containing branch and SWIFT code information.
     */
    public List<BranchListDTO> getSwiftCodes() {
        return swiftCodes;
    }
}
