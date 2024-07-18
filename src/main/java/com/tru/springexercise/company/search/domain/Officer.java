package com.tru.springexercise.company.search.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Officer {
    private String name;
    private String officer_role;
    private LocalDate appointed_on;
    private LocalDate resigned_on;
    private Address address;
    private String error;
}
