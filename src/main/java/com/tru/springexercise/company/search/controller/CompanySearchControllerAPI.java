package com.tru.springexercise.company.search.controller;

import com.tru.springexercise.company.search.domain.CompanySearchRequest;
import com.tru.springexercise.company.search.domain.CompanySearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
@RequestMapping("api/v1")
public interface CompanySearchControllerAPI {

    @GetMapping("/companies")
    public ResponseEntity<CompanySearchResponse> companySearch(@RequestBody
    CompanySearchRequest request, @RequestHeader("x-api-key") String apiKey);

}
