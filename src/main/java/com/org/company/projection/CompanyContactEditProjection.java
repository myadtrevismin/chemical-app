package com.org.company.projection;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.CompanyContact;

@Projection(name = "company_contact_edit", types = { CompanyContact.class })
public interface CompanyContactEditProjection {

	public long getId();

	public CompanyAutoSuggestProjection getCompany();

	public BranchAutoSuggestProjection getBranch();

	public boolean isStatus();

	public String getEmail();

	public String getPhone();

	public String getDepartment();

	public String getGender();

	public String getAboutWork();

	public String getReportsTo();

	public String getFirstMet();

	public String getWhatsapp();

	public String getWechat();

	public String getQq();

	public String getLinkedin();

	public Date getDob();

	public Date getAnniversary();

	public String getPreviousCompany();

	public String getName();
	
	public boolean isImage();
	
	public String getType();
}
