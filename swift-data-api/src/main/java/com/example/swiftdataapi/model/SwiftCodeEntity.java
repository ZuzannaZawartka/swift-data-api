package com.example.swiftdataapi.model;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
@Table(name = "swift_codes")
public class SwiftCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "countryISO2")
    private String countryISO2;

    @Column(name = "swiftCode")
    private String swiftCode;

    @Column(name = "bankName")
    private String bankName;

    @Column(name = "address")
    private String address;

    @Column(name = "countryName")
    private String countryName;

    @Column(name = "isHeadquarter")
    private boolean isHeadquarter;

    // Konstruktor bezargumentowy wymagany przez JPA
    public SwiftCodeEntity() {}

    // Konstruktor ze wszystkimi polami (bez codeType, townName, timeZone)
    public SwiftCodeEntity(Long id, String countryISO2, String swiftCode, String bankName,
                           String address, String countryName, boolean isHeadquarter) {
        this.id = id;
        this.countryISO2 = countryISO2;
        this.swiftCode = swiftCode;
        this.bankName = bankName;
        this.address = address;
        this.countryName = countryName;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean getHeadquarter() {
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
                ", bankName='" + bankName + '\'' +
                ", address='" + address + '\'' +
                ", countryName='" + countryName + '\'' +
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
                Objects.equals(bankName, that.bankName) &&
                Objects.equals(address, that.address) &&
                Objects.equals(countryName, that.countryName);
    }

    // Metoda hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, countryISO2, swiftCode, bankName, address, countryName, isHeadquarter);
    }
}
