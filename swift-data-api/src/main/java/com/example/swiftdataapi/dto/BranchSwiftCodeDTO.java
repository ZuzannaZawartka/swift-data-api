package com.example.swiftdataapi.dto;

public class BranchSwiftCodeDTO {

    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;
    private String swiftCode;

    public BranchSwiftCodeDTO(String address, String bankName, String countryISO2, String countryName, boolean isHeadquarter, String swiftCode) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.isHeadquarter = isHeadquarter;
        this.swiftCode = swiftCode;
    }

    // Gettery
    public String getAddress() {
        return address;
    }

    public String getBankName() {
        return bankName;
    }

    public String getCountryISO2() {
        return countryISO2;
    }

    public String getCountryName() {
        return countryName;
    }

    public boolean getHeadquarter() {
        return isHeadquarter;
    }

    public String getSwiftCode() {
        return swiftCode;
    }


}
