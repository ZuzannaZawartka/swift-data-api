package com.example.swiftdataapi.repository;

import com.example.swiftdataapi.model.SwiftCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCodeEntity, Long> {
}
