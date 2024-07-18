package com.tru.springexercise.company.search.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    private String premises;
    private String locality;
    private String address_line_1;
    private String country;
    private String postal_code;
}
