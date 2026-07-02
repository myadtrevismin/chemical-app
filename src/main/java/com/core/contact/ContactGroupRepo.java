package com.core.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.StringPath;

@Repository
@RepositoryRestResource(collectionResourceRel = "groups", path = "groups")
@PreAuthorize("isFullyAuthenticated()")
public interface ContactGroupRepo extends JpaRepository<ContactGroup, Long>, QuerydslPredicateExecutor<ContactGroup>,
		QuerydslBinderCustomizer<QContactGroup> {
	@Override
	default void customize(QuerydslBindings bindings, QContactGroup user) {
		bindings.bind(user.name).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}
}
