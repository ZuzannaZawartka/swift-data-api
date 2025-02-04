package com.example.swiftdataapi.repository;

import com.example.swiftdataapi.model.SwiftCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCodeEntity, Long> {

    Optional<SwiftCodeEntity> findBySwiftCode(String swiftCode);

    Optional<SwiftCodeEntity> findBySwiftCodeAndBankNameAndCountryISO2(String swiftCode, String bankName, String countryISO2);
}
