package com.portfolio.insurance.service;

import com.portfolio.insurance.domain.Customer;
import com.portfolio.insurance.domain.Insurer;
import com.portfolio.insurance.domain.Policy;
import com.portfolio.insurance.domain.PolicyStatus;
import com.portfolio.insurance.domain.PolicyType;
import com.portfolio.insurance.dto.PolicyRequest;
import com.portfolio.insurance.dto.PolicyResponse;
import com.portfolio.insurance.exception.BusinessException;
import com.portfolio.insurance.exception.NotFoundException;
import com.portfolio.insurance.mapper.PolicyMapper;
import com.portfolio.insurance.repository.CustomerRepository;
import com.portfolio.insurance.repository.InsurerRepository;
import com.portfolio.insurance.repository.PolicyRepository;
import com.portfolio.insurance.repository.spec.PolicySpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;
    private final InsurerRepository insurerRepository;

    @Transactional
    public PolicyResponse create(PolicyRequest request) {
        validateDates(request.startDate(), request.endDate());
        Customer customer = getCustomer(request.customerId());
        Insurer insurer = getInsurer(request.insurerId());
        Policy policy = PolicyMapper.toEntity(request, customer, insurer);
        recalculateStatus(policy);
        log.info("Criando apólice: {}", request.policyNumber());
        return PolicyMapper.toResponse(policyRepository.save(policy));
    }

    @Transactional
    public Page<PolicyResponse> list(PolicyStatus status, PolicyType type, UUID insurerId, UUID customerId,
                                    LocalDate endDateFrom, LocalDate endDateTo, String search, Pageable pageable) {
        Specification<Policy> spec = PolicySpecifications.hasStatus(status)
                .and(PolicySpecifications.hasType(type))
                .and(PolicySpecifications.hasInsurer(insurerId))
                .and(PolicySpecifications.hasCustomer(customerId))
                .and(PolicySpecifications.endDateFrom(endDateFrom))
                .and(PolicySpecifications.endDateTo(endDateTo))
                .and(PolicySpecifications.policyNumberContains(search));
        Page<Policy> page = policyRepository.findAll(spec, pageable);
        updateStatusesIfNeeded(page.getContent());
        return page.map(PolicyMapper::toResponse);
    }

    @Transactional
    public PolicyResponse get(UUID id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Apólice não encontrada"));
        boolean updated = recalculateStatus(policy);
        if (updated) {
            policyRepository.save(policy);
        }
        return PolicyMapper.toResponse(policy);
    }

    @Transactional
    public PolicyResponse update(UUID id, PolicyRequest request) {
        validateDates(request.startDate(), request.endDate());
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Apólice não encontrada"));
        Customer customer = getCustomer(request.customerId());
        Insurer insurer = getInsurer(request.insurerId());
        PolicyMapper.updateEntity(policy, request, customer, insurer);
        recalculateStatus(policy);
        return PolicyMapper.toResponse(policyRepository.save(policy));
    }

    @Transactional
    public void delete(UUID id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Apólice não encontrada"));
        policyRepository.delete(policy);
        log.info("Apólice removida: {}", id);
    }

    @Transactional
    public List<PolicyResponse> expiringPolicies(int days) {
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(days);
        Specification<Policy> spec = PolicySpecifications.endDateFrom(today)
                .and(PolicySpecifications.endDateTo(limit));
        List<Policy> policies = policyRepository.findAll(spec, org.springframework.data.domain.Sort.by("endDate").ascending());
        updateStatusesIfNeeded(policies);
        return policies.stream().map(PolicyMapper::toResponse).toList();
    }

    @Transactional
    public int recalculateStatuses() {
        List<Policy> policies = policyRepository.findAll();
        int updated = updateStatusesIfNeeded(policies);
        log.info("Recalculo de status finalizado. Atualizadas: {}", updated);
        return updated;
    }

    private int updateStatusesIfNeeded(List<Policy> policies) {
        List<Policy> updatedPolicies = new ArrayList<>();
        for (Policy policy : policies) {
            if (recalculateStatus(policy)) {
                updatedPolicies.add(policy);
            }
        }
        if (!updatedPolicies.isEmpty()) {
            policyRepository.saveAll(updatedPolicies);
        }
        return updatedPolicies.size();
    }

    private boolean recalculateStatus(Policy policy) {
        if (policy.getStatus() == PolicyStatus.CANCELADA) {
            return false;
        }
        PolicyStatus newStatus = LocalDate.now().isAfter(policy.getEndDate())
                ? PolicyStatus.VENCIDA
                : PolicyStatus.VIGENTE;
        if (policy.getStatus() != newStatus) {
            policy.setStatus(newStatus);
            return true;
        }
        return false;
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new BusinessException("Data fim deve ser maior ou igual à data início");
        }
    }

    private Customer getCustomer(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
    }

    private Insurer getInsurer(UUID id) {
        return insurerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seguradora não encontrada"));
    }
}
