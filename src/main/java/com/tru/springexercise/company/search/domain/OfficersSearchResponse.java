package com.tru.springexercise.company.search.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfficersSearchResponse {
    private String items_per_page;
    private List<Officer> items;
    private String error;
}
