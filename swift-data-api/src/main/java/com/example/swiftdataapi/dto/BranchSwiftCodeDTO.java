package com.example.swiftdataapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object (DTO) used for transferring data related to a bank branch
 * including address, bank name, country information, headquarter status, and SWIFT code.
 *
 * This class is used to encapsulate information needed to represent a branch's SWIFT code
 * details in various system operations (e.g., adding, updating, or viewing bank branch information).
 */
public class BranchSwiftCodeDTO {

    // The address of the bank branch
    private String address;

    // The name of the bank
    private String bankName;

    // The 2-letter ISO country code for the branch location
    private String countryISO2;

    // The name of the country where the branch is located
    private String countryName;

    // Flag indicating whether the branch is the headquarter of the bank
    @JsonProperty("headquarter")  // Ensures that when serialized to JSON, this field is named "headquarter"
    private boolean isHeadquarter;

    // The SWIFT code associated with the branch
    private String swiftCode;

    /**
     * Constructor for creating a BranchSwiftCodeDTO with all necessary data.
     *
     * @param address The address of the branch.
     * @param bankName The name of the bank.
     * @param countryISO2 The 2-letter ISO country code of the branch's location.
     * @param countryName The full name of the country.
     * @param isHeadquarter Whether this branch is the headquarter.
     * @param swiftCode The SWIFT code for the branch.
     */
    public BranchSwiftCodeDTO(String address, String bankName, String countryISO2, String countryName, boolean isHeadquarter, String swiftCode) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.isHeadquarter = isHeadquarter;
        this.swiftCode = swiftCode;
    }

    /**
     * Gets the address of the bank branch.
     *
     * @return The address of the branch.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the name of the bank.
     *
     * @return The name of the bank.
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * Gets the 2-letter ISO country code of the branch's location.
     *
     * @return The country ISO2 code.
     */
    public String getCountryISO2() {
        return countryISO2;
    }

    /**
     * Gets the full name of the country where the branch is located.
     *
     * @return The name of the country.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Gets the flag indicating whether this branch is the headquarter.
     *
     * @return true if the branch is the headquarter, false otherwise.
     */
    public boolean getHeadquarter() {
        return isHeadquarter;
    }

    /**
     * Gets the SWIFT code associated with the branch.
     *
     * @return The SWIFT code of the branch.
     */
    public String getSwiftCode() {
        return swiftCode;
    }
}
