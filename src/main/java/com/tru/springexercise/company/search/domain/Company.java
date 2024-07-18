package com.tru.springexercise.company.search.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private String company_number;
    private String company_type;
    private String title;
    private String company_status;
    private LocalDate date_of_creation;
    private Address address;
    private List<Officer> officers;
}
