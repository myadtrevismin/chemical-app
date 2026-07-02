package com.org.company.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.org.company.Company;
import com.org.company.QCompany;
import com.querydsl.core.types.dsl.StringPath;

@Repository
//@PreAuthorize("isFullyAuthenticated()")
@RepositoryRestResource(collectionResourceRel = "companies", path = "companies")
public interface CompanyRepo extends JpaRepository<Company, Long>,
QuerydslPredicateExecutor<Company>, QuerydslBinderCustomizer<QCompany>{
	@Override
	default void customize(QuerydslBindings bindings, QCompany obj) {
		bindings.bind(obj.creationDate).all((path, value) -> {
			List<? extends Date> dates = new ArrayList<>(value);
			if (dates.size() == 1) {
				return Optional.of(path.after(dates.get(0)));
			} else {
				Date from = dates.get(0);
				Date to = dates.get(1);
				return Optional.of(path.between(from, to));
			}
		});
		
		bindings.bind(obj.name).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}
}
