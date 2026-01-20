package com.portfolio.insurance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Dados para criação/atualização de seguradora")
public record InsurerRequest(
        @Schema(example = "Seguradora Atlas")
        @NotBlank(message = "Nome é obrigatório")
        String name,
        @Schema(example = "12345678000190")
        @Pattern(regexp = "^\\d{0,14}$", message = "CNPJ deve conter apenas dígitos")
        String cnpj
) {
}
