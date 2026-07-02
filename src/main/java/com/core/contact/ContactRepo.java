package com.core.contact;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.StringPath;

@Repository
@RepositoryRestResource(path = "contacts")
@PreAuthorize("isFullyAuthenticated()")
public interface ContactRepo
		extends JpaRepository<Contact, Long>, QuerydslPredicateExecutor<Contact>, QuerydslBinderCustomizer<QContact> {

	@Query("select t from Contact t where t.group.id in (:group)")
	List<Contact> findByGroup(@Param("group") Long group);

	@Override
	default void customize(QuerydslBindings bindings, QContact user) {
		bindings.bind(user.firstName).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}
}
