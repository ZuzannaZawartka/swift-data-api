package com.example.swiftdataapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Data Transfer Object (DTO) used for transferring information about a specific SWIFT code.
 *
 * This class encapsulates details about a bank's SWIFT code, including the address, bank name,
 * country information, headquarter status, SWIFT code itself, and a list of related branches.
 */
public class SwiftCodeResponseDTO {

    // The address of the bank or branch associated with this SWIFT code
    private String address;

    // The name of the bank
    private String bankName;

    // The 2-letter ISO country code where the bank/branch is located
    private String countryISO2;

    // The full name of the country where the bank/branch is located
    private String countryName;

    // A flag indicating whether this location is the headquarter of the bank
    @JsonProperty("headquarter")  // Ensures that this field is serialized as "headquarter"
    private boolean isHeadquarter;

    // The SWIFT code associated with the bank/branch
    private String swiftCode;

    // A list of branches related to this SWIFT code
    private List<BranchListDTO> branches;

    /**
     * Constructor for creating a SwiftCodeResponseDTO instance with all necessary fields.
     *
     * @param address The address of the bank/branch.
     * @param bankName The name of the bank.
     * @param countryISO2 The 2-letter ISO country code.
     * @param countryName The name of the country.
     * @param isHeadquarter Flag indicating if it's the headquarter.
     * @param swiftCode The SWIFT code for the bank/branch.
     * @param branches A list of branches related to the SWIFT code.
     */
    public SwiftCodeResponseDTO(String address, String bankName, String countryISO2, String countryName, boolean isHeadquarter, String swiftCode, List<BranchListDTO> branches) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.isHeadquarter = isHeadquarter;
        this.swiftCode = swiftCode;
        this.branches = branches;
    }

    /**
     * Gets the address associated with the SWIFT code.
     *
     * @return The address of the bank/branch.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the bank/branch.
     *
     * @param address The address to set.
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
     * @param bankName The name of the bank to set.
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * Gets the 2-letter ISO country code for the bank/branch location.
     *
     * @return The 2-letter country code.
     */
    public String getCountryISO2() {
        return countryISO2;
    }

    /**
     * Sets the 2-letter ISO country code for the bank/branch location.
     *
     * @param countryISO2 The 2-letter country code to set.
     */
    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    /**
     * Gets the name of the country where the bank/branch is located.
     *
     * @return The name of the country.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the name of the country where the bank/branch is located.
     *
     * @param countryName The country name to set.
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Gets the flag indicating whether this location is the headquarter.
     *
     * @return true if the location is the headquarter, false otherwise.
     */
    public boolean isHeadquarter() {
        return isHeadquarter;
    }

    /**
     * Sets the flag indicating if this location is the headquarter.
     *
     * @param headquarter The headquarter status to set.
     */
    public void setHeadquarter(boolean headquarter) {
        isHeadquarter = headquarter;
    }

    /**
     * Gets the SWIFT code associated with the bank/branch.
     *
     * @return The SWIFT code.
     */
    public String getSwiftCode() {
        return swiftCode;
    }

    /**
     * Sets the SWIFT code associated with the bank/branch.
     *
     * @param swiftCode The SWIFT code to set.
     */
    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    /**
     * Gets the list of branches related to the SWIFT code.
     *
     * @return A list of branches related to this SWIFT code.
     */
    public List<BranchListDTO> getBranches() {
        return branches;
    }

    /**
     * Sets the list of branches related to the SWIFT code.
     *
     * @param branches The list of branches to set.
     */
    public void setBranches(List<BranchListDTO> branches) {
        this.branches = branches;
    }
}
