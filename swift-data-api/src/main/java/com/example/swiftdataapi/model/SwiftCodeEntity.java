package com.example.swiftdataapi.model;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Represents a SWIFT code entity mapped to the "swift_codes" table in the database.
 * This class holds information about a bank's branch SWIFT code and its details.
 * The entity is used for storing and retrieving SWIFT code information in a database.
 */
@Entity
@Table(name = "swift_codes")
public class SwiftCodeEntity {

    // The primary key for the SwiftCodeEntity, auto-generated by the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // The 2-letter ISO country code (e.g., 'US', 'PL') for the branch's country
    @Column(name = "countryISO2")
    private String countryISO2;

    // The SWIFT code associated with the bank branch
    @Column(name = "swiftCode")
    private String swiftCode;

    // The name of the bank to which the SWIFT code belongs
    @Column(name = "bankName")
    private String bankName;

    // The physical address of the bank branch
    @Column(name = "address")
    private String address;

    // The full name of the country where the branch is located
    @Column(name = "countryName")
    private String countryName;

    // A flag indicating whether this branch is the headquarter of the bank
    @Column(name = "isHeadquarter")
    private boolean isHeadquarter;

    // Default constructor required by JPA for entity instantiation
    public SwiftCodeEntity() {}

    /**
     * Constructor to initialize a SwiftCodeEntity with the provided values.
     * This constructor is used for setting up the full entity with all the required fields.
     *
     * @param id The unique identifier for the SWIFT code entity
     * @param countryISO2 The 2-letter ISO country code
     * @param swiftCode The SWIFT code of the bank branch
     * @param bankName The name of the bank
     * @param address The address of the bank branch
     * @param countryName The full name of the country
     * @param isHeadquarter A flag indicating if this is the headquarter of the bank
     */
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

    // Getters and Setters

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

    // Method to generate a string representation of the entity
    // This method is helpful for logging or debugging the object
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

    // Method to check if two SwiftCodeEntity objects are equal
    // This is useful when comparing instances of this class in collections or tests
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

    // Method to generate a hash code for this entity
    // This is necessary for using the entity in hash-based collections like HashMap or HashSet
    @Override
    public int hashCode() {
        return Objects.hash(id, countryISO2, swiftCode, bankName, address, countryName, isHeadquarter);
    }
}
