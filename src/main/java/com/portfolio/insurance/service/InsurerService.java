package com.portfolio.insurance.service;

import com.portfolio.insurance.domain.Insurer;
import com.portfolio.insurance.dto.InsurerRequest;
import com.portfolio.insurance.dto.InsurerResponse;
import com.portfolio.insurance.exception.NotFoundException;
import com.portfolio.insurance.mapper.InsurerMapper;
import com.portfolio.insurance.repository.InsurerRepository;
import com.portfolio.insurance.repository.spec.InsurerSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsurerService {

    private final InsurerRepository insurerRepository;

    @Transactional
    public InsurerResponse create(InsurerRequest request) {
        log.info("Criando seguradora: {}", request.name());
        Insurer insurer = InsurerMapper.toEntity(request);
        return InsurerMapper.toResponse(insurerRepository.save(insurer));
    }

    @Transactional(readOnly = true)
    public Page<InsurerResponse> list(String name, Pageable pageable) {
        Specification<Insurer> spec = InsurerSpecifications.nameContains(name);
        return insurerRepository.findAll(spec, pageable).map(InsurerMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public InsurerResponse get(UUID id) {
        Insurer insurer = insurerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seguradora não encontrada"));
        return InsurerMapper.toResponse(insurer);
    }

    @Transactional
    public InsurerResponse update(UUID id, InsurerRequest request) {
        Insurer insurer = insurerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seguradora não encontrada"));
        InsurerMapper.updateEntity(insurer, request);
        return InsurerMapper.toResponse(insurerRepository.save(insurer));
    }

    @Transactional
    public void deactivate(UUID id) {
        Insurer insurer = insurerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seguradora não encontrada"));
        insurer.setActive(false);
        insurerRepository.save(insurer);
        log.info("Seguradora desativada: {}", id);
    }
}
