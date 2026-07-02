package com.org.company.projection;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.Company;

@Projection(name = "company_auto_suggest_product", types = { Company.class })
public interface CompanyAutoSuggestProductProjection {
	long getId();

	String getName();

	String getEmail();

	String getPhone();

	List<CompanyProductProjection> getProducts();
}
