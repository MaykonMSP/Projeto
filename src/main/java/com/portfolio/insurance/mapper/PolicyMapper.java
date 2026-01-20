package com.portfolio.insurance.mapper;

import com.portfolio.insurance.domain.Customer;
import com.portfolio.insurance.domain.Insurer;
import com.portfolio.insurance.domain.Policy;
import com.portfolio.insurance.dto.PolicyRequest;
import com.portfolio.insurance.dto.PolicyResponse;

public class PolicyMapper {

    public static Policy toEntity(PolicyRequest request, Customer customer, Insurer insurer) {
        Policy policy = new Policy();
        policy.setPolicyNumber(request.policyNumber());
        policy.setType(request.type());
        policy.setStatus(request.status());
        policy.setStartDate(request.startDate());
        policy.setEndDate(request.endDate());
        policy.setMonthlyPremium(request.monthlyPremium());
        policy.setNotes(request.notes());
        policy.setCustomer(customer);
        policy.setInsurer(insurer);
        return policy;
    }

    public static void updateEntity(Policy policy, PolicyRequest request, Customer customer, Insurer insurer) {
        policy.setPolicyNumber(request.policyNumber());
        policy.setType(request.type());
        policy.setStatus(request.status());
        policy.setStartDate(request.startDate());
        policy.setEndDate(request.endDate());
        policy.setMonthlyPremium(request.monthlyPremium());
        policy.setNotes(request.notes());
        policy.setCustomer(customer);
        policy.setInsurer(insurer);
    }

    public static PolicyResponse toResponse(Policy policy) {
        return new PolicyResponse(
                policy.getId(),
                policy.getPolicyNumber(),
                policy.getType(),
                policy.getStatus(),
                policy.getStartDate(),
                policy.getEndDate(),
                policy.getMonthlyPremium(),
                policy.getNotes(),
                policy.getCustomer().getId(),
                policy.getCustomer().getFullName(),
                policy.getInsurer().getId(),
                policy.getInsurer().getName(),
                policy.getCreatedAt(),
                policy.getUpdatedAt()
        );
    }
}
