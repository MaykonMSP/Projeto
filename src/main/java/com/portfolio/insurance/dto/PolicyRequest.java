package com.portfolio.insurance.dto;

import com.portfolio.insurance.domain.PolicyStatus;
import com.portfolio.insurance.domain.PolicyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados para criação/atualização de apólice")
public record PolicyRequest(
        @Schema(example = "POL-2024-0001")
        @NotBlank(message = "Número da apólice é obrigatório")
        String policyNumber,
        @NotNull(message = "Tipo é obrigatório")
        PolicyType type,
        @NotNull(message = "Status é obrigatório")
        PolicyStatus status,
        @NotNull(message = "Data início é obrigatória")
        LocalDate startDate,
        @NotNull(message = "Data fim é obrigatória")
        LocalDate endDate,
        @DecimalMin(value = "0.0", inclusive = false, message = "Prêmio deve ser positivo")
        BigDecimal monthlyPremium,
        String notes,
        @NotNull(message = "Cliente é obrigatório")
        UUID customerId,
        @NotNull(message = "Seguradora é obrigatória")
        UUID insurerId
) {
}
