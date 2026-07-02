package com.core.category.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;

import com.core.category.Category;
import com.core.category.QCategory;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@RepositoryRestResource(path = "categories")
public interface CategoryRepo extends JpaRepository<Category, Long>, QuerydslPredicateExecutor<Category>,
		QuerydslBinderCustomizer<QCategory> {
	@Override
	default void customize(QuerydslBindings bindings, QCategory user) {
		bindings.bind(user.name).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});

		bindings.bind(user.lineage).first((path, value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else if (value.equals("null")) {
				return path.isNull();
			} else {
				return path.eq(value);
			}
		});
	}

	@Secured({ "ROLE_ADMIN" })
	<S extends Category> S save(S s);

	@Secured({ "ROLE_ADMIN" })
	void deleteById(Long aLong);

	@Secured({ "ROLE_ADMIN" })
	@RestResource(exported = false)
	void deleteAll(Iterable<? extends Category> orders);

}
