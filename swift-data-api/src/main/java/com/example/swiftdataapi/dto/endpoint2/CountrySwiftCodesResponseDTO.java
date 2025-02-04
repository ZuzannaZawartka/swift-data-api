package com.example.swiftdataapi.dto.endpoint2;

import com.example.swiftdataapi.dto.BranchListDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountrySwiftCodesResponseDTO {
    private String countryISO2;
    private String countryName;
    private List<BranchListDTO> swiftCodes;

    public CountrySwiftCodesResponseDTO(String countryISO2, String countryName, List<BranchListDTO> swiftCodes) {
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.swiftCodes = swiftCodes;
    }

    public String getCountryISO2() {
        return countryISO2;
    }

    public String getCountryName() {
        return countryName;
    }

    public List<BranchListDTO> getSwiftCodes() {
        return swiftCodes;
    }
}
