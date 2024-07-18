package com.tru.springexercise.company.search.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanySearchRequest {
    private String companyName;
    private String companyNumber;
    private boolean isActive;
}