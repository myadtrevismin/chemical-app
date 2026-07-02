package com.core.role.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.core.role.QUserBasePermission;
import com.core.role.UserBasePermission;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@Secured({ "ROLE_ADMIN" })
@PreAuthorize("isFullyAuthenticated()")
@RepositoryRestResource(collectionResourceRel = "permissions", path = "permissions")
public interface UserBasePermissionRepo extends JpaRepository<UserBasePermission, Long>,
		QuerydslPredicateExecutor<UserBasePermission>, QuerydslBinderCustomizer<QUserBasePermission> {
	@Override
	default void customize(QuerydslBindings bindings, QUserBasePermission obj) {
		bindings.bind(obj.description).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}
}
