package com.example.swiftdataapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object (DTO) representing a bank branch and its details.
 *
 * This class is used to store information about a specific bank branch, including
 * its address, the name of the bank, its country, whether it is the headquarter,
 * and its associated SWIFT code.
 */
public class BranchListDTO {

    // The address of the bank branch
    private String address;

    // The name of the bank
    private String bankName;

    // The 2-letter ISO code representing the country of the branch
    private String countryISO2;

    // Boolean flag indicating if the branch is the headquarter of the bank
    @JsonProperty("headquarter")  // Ensures the JSON property is named "headquarter" when serialized
    private boolean isHeadquarter;

    // The SWIFT code for the branch
    private String swiftCode;

    /**
     * Constructor to initialize a BranchListDTO object with the given details.
     *
     * @param address The address of the bank branch.
     * @param bankName The name of the bank.
     * @param countryISO2 The 2-letter ISO country code of the branch location.
     * @param isHeadquarter A flag indicating whether this branch is the headquarter.
     * @param swiftCode The SWIFT code for the branch.
     */
    public BranchListDTO(String address, String bankName, String countryISO2, boolean isHeadquarter, String swiftCode) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
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
     * Sets the address of the bank branch.
     *
     * @param address The address to set for the branch.
     */
    public void setAddress(String address) {
        this.address = address;
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
     * Sets the name of the bank.
     *
     * @param bankName The bank's name to set.
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * Gets the 2-letter ISO country code of the branch.
     *
     * @return The ISO2 country code.
     */
    public String getCountryISO2() {
        return countryISO2;
    }

    /**
     * Sets the 2-letter ISO country code of the branch.
     *
     * @param countryISO2 The ISO2 country code to set.
     */
    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    /**
     * Gets the flag indicating whether the branch is the headquarter.
     *
     * @return true if the branch is the headquarter, false otherwise.
     */
    public boolean getHeadquarter() {
        return isHeadquarter;
    }

    /**
     * Sets the flag indicating whether the branch is the headquarter.
     *
     * @param headquarter Set to true if the branch is the headquarter, false otherwise.
     */
    public void setHeadquarter(boolean headquarter) {
        isHeadquarter = headquarter;
    }

    /**
     * Gets the SWIFT code of the branch.
     *
     * @return The SWIFT code.
     */
    public String getSwiftCode() {
        return swiftCode;
    }

    /**
     * Sets the SWIFT code for the branch.
     *
     * @param swiftCode The SWIFT code to set.
     */
    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
}
