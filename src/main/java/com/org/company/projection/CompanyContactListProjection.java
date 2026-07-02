package com.org.company.projection;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.CompanyContact;

@Projection(name = "company_contact_list", types = { CompanyContact.class })
public interface CompanyContactListProjection {
	long getId();

	CompanyAutoSuggestProjection getCompany();

	BranchAutoSuggestProjection getBranch();

	boolean isStatus();

	String getEmail();

	String getPhone();
	
	String getType();

	String getGender();

	String getWhatsapp();

	String getName();
	
	public boolean isImage();
	
	public Date getCreationDate();
}
