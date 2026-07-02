package com.org.company.projection;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.CompanyBranch;

@Projection(name = "branch_auto_suggest", types = { CompanyBranch.class })
public interface BranchAutoSuggestProjection {
	long getId();

	String getName();
}
