package com.tru.springexercise.company.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.tru.springexercise.company.search" })
public class CompanySearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanySearchApplication.class, args);
	}

}
