package com.tru.springexercise.company.search.service;

import com.tru.springexercise.company.search.domain.Company;
import com.tru.springexercise.company.search.domain.OfficersSearchResponse;
import com.tru.springexercise.company.search.domain.CompanySearchRequest;
import com.tru.springexercise.company.search.domain.CompanySearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CompanySearchService {

    public ResponseEntity<CompanySearchResponse> getCompanyDetails
            (CompanySearchRequest request, String apiKey);

    public ResponseEntity<OfficersSearchResponse> getOfficerDetails
            (Company request, String apiKey);
}
