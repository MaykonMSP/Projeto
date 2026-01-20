package com.portfolio.insurance.repository;

import com.portfolio.insurance.domain.Insurer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface InsurerRepository extends JpaRepository<Insurer, UUID>, JpaSpecificationExecutor<Insurer> {
}
