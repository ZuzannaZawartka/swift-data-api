package com.example.swiftdataapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "swift_codes")
public class SwiftCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String countryISO2;
    private String swiftCode;
    private String codeType;
    private String bankName;
    private String address;
    private String townName;
    private String countryName;
    private String timeZone;
    private boolean isHeadquarter;

    // Konstruktor bezargumentowy wymagany przez JPA
    public SwiftCodeEntity() {}

    // Konstruktor ze wszystkimi polami
    public SwiftCodeEntity(Long id, String countryISO2, String swiftCode, String codeType,
                           String bankName, String address, String townName,
                           String countryName, String timeZone, boolean isHeadquarter) {
        this.id = id;
        this.countryISO2 = countryISO2;
        this.swiftCode = swiftCode;
        this.codeType = codeType;
        this.bankName = bankName;
        this.address = address;
        this.townName = townName;
        this.countryName = countryName;
        this.timeZone = timeZone;
        this.isHeadquarter = isHeadquarter;
    }

    // Gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryISO2() {
        return countryISO2;
    }

    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isHeadquarter() {
        return isHeadquarter;
    }

    public void setHeadquarter(boolean headquarter) {
        isHeadquarter = headquarter;
    }

    // Metoda toString
    @Override
    public String toString() {
        return "SwiftCodeEntity{" +
                "id=" + id +
                ", countryISO2='" + countryISO2 + '\'' +
                ", swiftCode='" + swiftCode + '\'' +
                ", codeType='" + codeType + '\'' +
                ", bankName='" + bankName + '\'' +
                ", address='" + address + '\'' +
                ", townName='" + townName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", isHeadquarter=" + isHeadquarter +
                '}';
    }

    // Metoda equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwiftCodeEntity that = (SwiftCodeEntity) o;
        return isHeadquarter == that.isHeadquarter &&
                Objects.equals(id, that.id) &&
                Objects.equals(countryISO2, that.countryISO2) &&
                Objects.equals(swiftCode, that.swiftCode) &&
                Objects.equals(codeType, that.codeType) &&
                Objects.equals(bankName, that.bankName) &&
                Objects.equals(address, that.address) &&
                Objects.equals(townName, that.townName) &&
                Objects.equals(countryName, that.countryName) &&
                Objects.equals(timeZone, that.timeZone);
    }

    // Metoda hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, countryISO2, swiftCode, codeType, bankName, address, townName, countryName, timeZone, isHeadquarter);
    }
}
