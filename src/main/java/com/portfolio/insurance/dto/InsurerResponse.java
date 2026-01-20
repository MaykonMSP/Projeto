package com.portfolio.insurance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados de seguradora")
public record InsurerResponse(
        UUID id,
        String name,
        String cnpj,
        boolean active
) {
}
