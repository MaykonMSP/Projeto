package com.portfolio.insurance.service;

import com.portfolio.insurance.domain.Customer;
import com.portfolio.insurance.domain.Insurer;
import com.portfolio.insurance.domain.Policy;
import com.portfolio.insurance.domain.PolicyStatus;
import com.portfolio.insurance.domain.PolicyType;
import com.portfolio.insurance.repository.CustomerRepository;
import com.portfolio.insurance.repository.InsurerRepository;
import com.portfolio.insurance.repository.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PolicyServiceTest {

    @Autowired
    private PolicyService policyService;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InsurerRepository insurerRepository;

    @Test
    void shouldRecalculateStatusRespectingCancelled() {
        Customer customer = new Customer();
        customer.setFullName("Maria Souza");
        customer.setCpf("12345678901");
        customerRepository.save(customer);

        Insurer insurer = new Insurer();
        insurer.setName("Seguradora Teste");
        insurerRepository.save(insurer);

        Policy expiredPolicy = new Policy();
        expiredPolicy.setPolicyNumber("POL-EXPIRED");
        expiredPolicy.setType(PolicyType.VIDA);
        expiredPolicy.setStatus(PolicyStatus.VIGENTE);
        expiredPolicy.setStartDate(LocalDate.now().minusDays(10));
        expiredPolicy.setEndDate(LocalDate.now().minusDays(1));
        expiredPolicy.setCustomer(customer);
        expiredPolicy.setInsurer(insurer);
        policyRepository.save(expiredPolicy);

        Policy cancelledPolicy = new Policy();
        cancelledPolicy.setPolicyNumber("POL-CANCELLED");
        cancelledPolicy.setType(PolicyType.AUTO);
        cancelledPolicy.setStatus(PolicyStatus.CANCELADA);
        cancelledPolicy.setStartDate(LocalDate.now().minusDays(5));
        cancelledPolicy.setEndDate(LocalDate.now().minusDays(1));
        cancelledPolicy.setCustomer(customer);
        cancelledPolicy.setInsurer(insurer);
        policyRepository.save(cancelledPolicy);

        int updated = policyService.recalculateStatuses();

        Policy updatedExpired = policyRepository.findById(expiredPolicy.getId()).orElseThrow();
        Policy updatedCancelled = policyRepository.findById(cancelledPolicy.getId()).orElseThrow();

        assertThat(updated).isEqualTo(1);
        assertThat(updatedExpired.getStatus()).isEqualTo(PolicyStatus.VENCIDA);
        assertThat(updatedCancelled.getStatus()).isEqualTo(PolicyStatus.CANCELADA);
    }
}
