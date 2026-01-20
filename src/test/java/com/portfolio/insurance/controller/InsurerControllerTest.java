package com.portfolio.insurance.controller;

import com.portfolio.insurance.dto.InsurerResponse;
import com.portfolio.insurance.service.InsurerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InsurerController.class)
@Import(com.portfolio.insurance.config.SecurityConfig.class)
class InsurerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsurerService insurerService;

    @Test
    @WithMockUser(roles = "USER")
    void shouldListInsurers() throws Exception {
        InsurerResponse response = new InsurerResponse(UUID.randomUUID(), "Seguradora XP", "12345678000190", true);
        Page<InsurerResponse> page = new PageImpl<>(List.of(response));
        when(insurerService.list(any(), any())).thenReturn(page);

        mockMvc.perform(get("/insurers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Seguradora XP"));
    }
}
