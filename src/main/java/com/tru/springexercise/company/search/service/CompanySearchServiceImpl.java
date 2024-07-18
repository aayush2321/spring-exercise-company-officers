package com.tru.springexercise.company.search.service;

import com.tru.springexercise.company.search.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CompanySearchServiceImpl implements CompanySearchService{

    Logger logger = LoggerFactory.getLogger(CompanySearchServiceImpl.class);
    @Autowired
    RestTemplate restTemplate;

    @Value("${truProxy.api.companySearchTRUProxyURL}")
    private String companySearchTRUProxyURL;

    @Value("${truProxy.api.officerSearchTRUProxyURL}")
    private String officerSearchTRUProxyURL;

    @Override
    public ResponseEntity<CompanySearchResponse> getCompanyDetails
            (CompanySearchRequest request, String apiKey) {
        logger.debug("Inside getCompanyDetails method");
        //set URL
        String companySearchURL = companySearchTRUProxyURL +
                (StringUtils.isNotBlank(request.getCompanyNumber()) ?
                        request.getCompanyNumber() : request.getCompanyName());

        //set header
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        logger.debug("Calling companySearch proxy endpoint for companyNumber {} & companyName {}", request.getCompanyNumber(), request.getCompanyName());
        //API call search company
        ResponseEntity<CompanySearchResponse> companySearchProxyResponse =
                restTemplate.exchange(companySearchURL, HttpMethod.GET, entity,
                        CompanySearchResponse.class);

        //if company not found
        if(Objects.isNull(companySearchProxyResponse.getBody().getItems())) {
            logger.debug("Company not found");
            companySearchProxyResponse.getBody().setError("Company not found");
            return new ResponseEntity<>(companySearchProxyResponse.getBody(),
                    HttpStatus.BAD_REQUEST);
        }
        logger.debug("Company(s) found");

        //filter active companies if isActive boolean is true
        if(request.isActive()) {
            companySearchProxyResponse.getBody().setItems(companySearchProxyResponse.getBody().getItems().stream()
                    .filter(company -> company.getCompany_status().equalsIgnoreCase("active"))
                    .collect(Collectors.toList()));
            companySearchProxyResponse.getBody().setTotal_results(companySearchProxyResponse.getBody().getItems().size());
        }

        //else go ahead and pull company officers
        int itemNumber = 0;
        for(Company company : companySearchProxyResponse.getBody().getItems()) {
            //get company officers
            ResponseEntity<OfficersSearchResponse> officerSearchResponse = getOfficerDetails(company, apiKey);

            //process the company and officers details
            List<Officer> officers = processOfficerSearchResponse(companySearchProxyResponse, officerSearchResponse);
            companySearchProxyResponse.getBody().getItems().get(itemNumber).setOfficers(officers);
            itemNumber++;
        }

        //output
        return new ResponseEntity<>(companySearchProxyResponse.getBody(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OfficersSearchResponse> getOfficerDetails(Company company, String apiKey) {
        logger.debug("Calling officerSearch proxy endpoint for company number: {}", company.getCompany_number());
        //create api url
        String officerSearchURL = officerSearchTRUProxyURL + company.getCompany_number();
        //set header
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        //call officers search
        ResponseEntity<OfficersSearchResponse> officerSearchResponse = restTemplate.exchange(
                officerSearchURL, HttpMethod.GET, entity, OfficersSearchResponse.class);
        if(!officerSearchResponse.getStatusCode().is2xxSuccessful() || Objects.isNull(officerSearchResponse.getBody().getItems())) {
            logger.debug("Officers not found");
            officerSearchResponse.getBody().setError("Officers not found");
            return new ResponseEntity<>(officerSearchResponse.getBody(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(officerSearchResponse.getBody(), HttpStatus.OK);
    }

    public List<Officer> processOfficerSearchResponse(ResponseEntity<CompanySearchResponse> companySearchProxyResponse,
                                 ResponseEntity<OfficersSearchResponse> officerSearchResponse) {
        logger.debug("Processing officerSearch response");
        List<Officer> officerList = new ArrayList<>();
        if (!Objects.isNull(officerSearchResponse.getBody().getError())) {
            Officer officer = Officer.builder()
                    .error(officerSearchResponse.getBody().getError())
                    .build();
            officerList.add(officer);
        } else {
            for (Officer officer : officerSearchResponse.getBody().getItems()) {
                if (Objects.isNull(officer.getResigned_on())) {
                    officerList.add(officer);
                }
            }
        }
        return officerList;
    }
}
