package com.portfolio.insurance.mapper;

import com.portfolio.insurance.domain.Insurer;
import com.portfolio.insurance.dto.InsurerRequest;
import com.portfolio.insurance.dto.InsurerResponse;

public class InsurerMapper {

    public static Insurer toEntity(InsurerRequest request) {
        Insurer insurer = new Insurer();
        insurer.setName(request.name());
        insurer.setCnpj(request.cnpj());
        return insurer;
    }

    public static void updateEntity(Insurer insurer, InsurerRequest request) {
        insurer.setName(request.name());
        insurer.setCnpj(request.cnpj());
    }

    public static InsurerResponse toResponse(Insurer insurer) {
        return new InsurerResponse(
                insurer.getId(),
                insurer.getName(),
                insurer.getCnpj(),
                insurer.isActive()
        );
    }
}
