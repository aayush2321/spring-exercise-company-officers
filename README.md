Submitted by Aayush Sharma (aayush2321@gmail.com)

Added Swagger API documentation for API description
http://localhost:8080/swagger-ui/index.html

Application endpoint (GET): http://localhost:8080/api/v1/companies
(Using body with GET rather than using POST as it felt intuitive while trying to GET information)

Sample request:
{
"companyName" : "BBC LIMITED",
"companyNumber" : "06500244",
"active": true
}

Note1: Pass header "x-api-key" to get result

Note2: Update port from application.properties if default port 8080 is busy

About:
CompanySearchControllerImpl calls CompanySearchService.
CompanySearchService calls TruProxyAPI to get Company & Officers details using company number or name.
If active is set to true in request json, then companies with active status are passed in the response.
getOfficers endpoint is called only if getCompanies endpoint returns company data.
Officers for each company are included in the response (only if they are active).

Error message is added to response if requested company is not found.
Error message is added to response if officer is not found for an existing company.

Tests using  JUnit 5 & Mockito:
Unit tests are added for service class to test positive and negative scenarios, achieving 100% method & 80% line coverage.
Integration tests are added for controller class.


Futher improvements:
1. Validation for request parameters
2. Secure application with basic auth
3. remove underscore from variable names in domain classes and use @JsonProperty to denote the name.