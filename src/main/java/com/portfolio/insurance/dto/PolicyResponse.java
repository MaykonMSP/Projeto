package com.portfolio.insurance.dto;

import com.portfolio.insurance.domain.PolicyStatus;
import com.portfolio.insurance.domain.PolicyType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de apólice")
public record PolicyResponse(
        UUID id,
        String policyNumber,
        PolicyType type,
        PolicyStatus status,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal monthlyPremium,
        String notes,
        UUID customerId,
        String customerName,
        UUID insurerId,
        String insurerName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
