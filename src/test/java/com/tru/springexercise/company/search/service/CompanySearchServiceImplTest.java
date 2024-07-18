package com.tru.springexercise.company.search.service;


import com.tru.springexercise.company.search.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CompanySearchServiceImplTest {

    @InjectMocks
    private CompanySearchServiceImpl companySearchService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCompanyDetails_companyFound() {
        String apiKey = "TestApiKey";
        OfficersSearchResponse mockOfficers = new OfficersSearchResponse();
        Officer officer = Officer.builder().name("Frank").build();
        mockOfficers.setItems(Collections.singletonList(officer));
        ResponseEntity<OfficersSearchResponse> officerResponseEntity =
                new ResponseEntity<>(mockOfficers, HttpStatus.OK);

        CompanySearchRequest request = CompanySearchRequest.builder()
                .companyName("TestCompanyName").build();

        CompanySearchResponse mockResponse = new CompanySearchResponse();
        Company company = Company.builder()
                .company_number("1234578")
                .title("TestCompanyName")
                .company_status("active").build();
        mockResponse.setItems(Collections.singletonList(company));

        ResponseEntity<CompanySearchResponse> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(OfficersSearchResponse.class)))
                .thenReturn(officerResponseEntity);

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET),
                any(HttpEntity.class), eq(CompanySearchResponse.class)))
                .thenReturn(responseEntity);

        ResponseEntity<CompanySearchResponse> response =
                companySearchService.getCompanyDetails(request, apiKey);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getItems().size());
    }

    @Test
    void testGetCompanyDetails_companyNotFound() {
        String apiKey = "TestApiKey";
        CompanySearchRequest request = CompanySearchRequest.builder()
                .companyName("NoCompanyFound").build();

        CompanySearchResponse mockResponse = new CompanySearchResponse();

        ResponseEntity<CompanySearchResponse> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET),
                any(HttpEntity.class), eq(CompanySearchResponse.class)))
                .thenReturn(responseEntity);

        ResponseEntity<CompanySearchResponse> response =
                companySearchService.getCompanyDetails(request, apiKey);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Company not found", response.getBody().getError());
    }

    @Test
    void testGetOfficerDetails_officersFound() {
        Company company = Company.builder().company_number("12345678").build();
        String apiKey = "TestApiKey";

        OfficersSearchResponse mockOfficers = new OfficersSearchResponse();
        Officer officer = Officer.builder().name("Frank").build();
        mockOfficers.setItems(Collections.singletonList(officer));

        ResponseEntity<OfficersSearchResponse> responseEntity =
                new ResponseEntity<>(mockOfficers, HttpStatus.OK);

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET),
                any(HttpEntity.class), eq(OfficersSearchResponse.class)))
                .thenReturn(responseEntity);

        ResponseEntity<OfficersSearchResponse> response =
                companySearchService.getOfficerDetails(company, apiKey);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getItems().size());
    }

    @Test
    void TestGetOfficerDetails_officersNotFound() {
        Company company = Company.builder().company_number("12345777").build();
        String apiKey = "TestApiKey";

        OfficersSearchResponse mockOfficers = new OfficersSearchResponse();

        ResponseEntity<OfficersSearchResponse> responseEntity =
                new ResponseEntity<>(mockOfficers, HttpStatus.OK);

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET),
                any(HttpEntity.class), eq(OfficersSearchResponse.class)))
                .thenReturn(responseEntity);

        ResponseEntity<OfficersSearchResponse> response =
                companySearchService.getOfficerDetails(company, apiKey);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Officers not found", response.getBody().getError());
    }

    @Test
    void testProcessOfficerSearchResponse_officersResigned() {
        CompanySearchResponse companySearchResponse = new CompanySearchResponse();
        Company company = Company.builder()
                .company_number("1234578")
                .title("TestCompanyName")
                .company_status("active").build();
        companySearchResponse.setItems(Collections.singletonList(company));

        OfficersSearchResponse officersSearchResponse = new OfficersSearchResponse();
        Officer officer = Officer.builder().name("Old Frank").resigned_on(LocalDate.now()).build();
        officersSearchResponse.setItems(Collections.singletonList(officer));

        ResponseEntity<CompanySearchResponse> companySearchResponseEntity =
                new ResponseEntity<>(companySearchResponse, HttpStatus.OK);
        ResponseEntity<OfficersSearchResponse> companyOfficersResponseEntity =
                new ResponseEntity<>(officersSearchResponse, HttpStatus.OK);

        List<Officer> officerList = companySearchService.processOfficerSearchResponse(companySearchResponseEntity, companyOfficersResponseEntity);

        assertTrue(officerList.isEmpty());
    }

}