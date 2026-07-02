package com.org.company.projection;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.CompanyContact;

@Projection(name = "company_contact_name", types = { CompanyContact.class })
public interface CompanyContactNameProjection {
	long getId();

	String getEmail();

	String getPhone();

	String getName();
}
