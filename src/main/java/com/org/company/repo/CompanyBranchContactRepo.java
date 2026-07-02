package com.org.company.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.org.company.CompanyBranchContact;

@Repository
@PreAuthorize("isFullyAuthenticated()")
@RepositoryRestResource(collectionResourceRel = "branch-contacts", path = "branch-contacts")
public interface CompanyBranchContactRepo
		extends JpaRepository<CompanyBranchContact, Long>, QuerydslPredicateExecutor<CompanyBranchContact> {
}
