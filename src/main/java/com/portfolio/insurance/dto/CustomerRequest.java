package com.portfolio.insurance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Schema(description = "Dados para criação/atualização de cliente")
public record CustomerRequest(
        @Schema(example = "João da Silva")
        @NotBlank(message = "Nome completo é obrigatório")
        String fullName,
        @Schema(example = "12345678901")
        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter 11 dígitos")
        String cpf,
        @Schema(example = "joao@email.com")
        @Email(message = "Email inválido")
        String email,
        @Schema(example = "+55 11 99999-0000")
        String phone,
        @Schema(example = "1990-05-20")
        LocalDate birthDate
) {
}
