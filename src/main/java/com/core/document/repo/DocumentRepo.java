package com.core.document.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.core.document.service.Document;
import com.core.document.service.QDocument;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@PreAuthorize("isFullyAuthenticated()")
@RepositoryRestResource(collectionResourceRel = "docs", path = "docs")
public interface DocumentRepo extends JpaRepository<Document, Long>,
QuerydslPredicateExecutor<Document>, QuerydslBinderCustomizer<QDocument> {
	@Override
	default void customize(QuerydslBindings bindings, QDocument obj) {
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
		bindings.bind(obj.fileName).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}
	
	@RestResource(exported = false)
	void deleteById(Long id);
}
