package com.org.company.projection;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.Company;

@Projection(name = "company_auto_suggest_order", types = { Company.class })
public interface CompanyAutoSuggestOrderProjection {
	long getId();

	String getName();

	String getPaymentTerms();
}
