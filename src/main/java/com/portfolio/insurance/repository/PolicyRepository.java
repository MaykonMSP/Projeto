package com.portfolio.insurance.repository;

import com.portfolio.insurance.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PolicyRepository extends JpaRepository<Policy, UUID>, JpaSpecificationExecutor<Policy> {
}
