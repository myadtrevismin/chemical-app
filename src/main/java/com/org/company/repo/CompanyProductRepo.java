package com.org.company.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.org.company.CompanyProduct;

@Repository
@PreAuthorize("isFullyAuthenticated()")
@RepositoryRestResource(collectionResourceRel = "company-products", path = "company-products")
public interface CompanyProductRepo
		extends JpaRepository<CompanyProduct, Long>, QuerydslPredicateExecutor<CompanyProduct> {
}
