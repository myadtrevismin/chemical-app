package com.core.role.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.core.role.QUserBaseRole;
import com.core.role.UserBaseRole;
import com.querydsl.core.types.dsl.StringPath;

@Repository
//@Secured({ "ROLE_ADMIN" })
//@PreAuthorize("isFullyAuthenticated()")
@RepositoryRestResource(collectionResourceRel = "roles", path = "roles")
public interface UserBaseRoleRepo extends JpaRepository<UserBaseRole, Long>, QuerydslPredicateExecutor<UserBaseRole>,
		QuerydslBinderCustomizer<QUserBaseRole> {
	@Override
	default void customize(QuerydslBindings bindings, QUserBaseRole obj) {
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
