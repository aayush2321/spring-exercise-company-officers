package com.tru.springexercise.company.search.controller;

import com.tru.springexercise.company.search.domain.CompanySearchRequest;
import com.tru.springexercise.company.search.domain.CompanySearchResponse;
import com.tru.springexercise.company.search.service.CompanySearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanySearchControllerImpl implements CompanySearchControllerAPI{

    Logger logger = LoggerFactory.getLogger(CompanySearchControllerImpl.class);
    @Autowired
    CompanySearchService companySearchService;

    @Override
    public ResponseEntity<CompanySearchResponse> companySearch(CompanySearchRequest request, String apiKey) {
        logger.debug("Executing companySearch");
        return companySearchService.getCompanyDetails(request, apiKey);
    }
}
