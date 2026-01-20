package com.portfolio.insurance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de cliente")
public record CustomerResponse(
        UUID id,
        String fullName,
        String cpf,
        String email,
        String phone,
        LocalDate birthDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
