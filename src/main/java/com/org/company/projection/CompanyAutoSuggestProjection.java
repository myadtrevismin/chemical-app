package com.org.company.projection;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.Company;

@Projection(name = "company_auto_suggest", types = { Company.class })
public interface CompanyAutoSuggestProjection {
	long getId();

	String getName();
}
