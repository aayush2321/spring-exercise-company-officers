package com.tru.springexercise.company.search.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tru.springexercise.company.search.domain.Company;
import com.tru.springexercise.company.search.domain.CompanySearchRequest;
import com.tru.springexercise.company.search.domain.CompanySearchResponse;
import com.tru.springexercise.company.search.domain.Officer;
import com.tru.springexercise.company.search.service.CompanySearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanySearchControllerImpl.class)
class CompanySearchControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanySearchService companySearchService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void TestCompanySearch_companyFound() throws Exception {
        CompanySearchRequest request = CompanySearchRequest.builder()
                .companyName("TestCompanyName").build();

        CompanySearchResponse mockResponse = new CompanySearchResponse();
        Company company = Company.builder()
                .company_number("1234578")
                .title("TestCompanyName")
                .officers(Collections.singletonList(Officer.builder().name("Frank").build()))
                .company_status("active").build();
        mockResponse.setItems(Collections.singletonList(company));

        when(companySearchService.getCompanyDetails(any(CompanySearchRequest.class), eq("TestApiKey")))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        mockMvc.perform(get("/api/v1/companies")
                        .header("x-api-key", "TestApiKey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    void TestCompanySearch_companyNotFound() throws Exception {
        CompanySearchRequest request = CompanySearchRequest.builder()
                .companyName("NoCompanyFound").build();

        CompanySearchResponse mockResponse = CompanySearchResponse.builder()
                .error("Company not found").build();

        when(companySearchService.getCompanyDetails(any(CompanySearchRequest.class), eq("TestApiKey")))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/api/v1/companies")
                        .header("x-api-key", "TestApiKey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

}