package com.core.location.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.core.location.Location;
import com.core.location.QLocation;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@RepositoryRestResource(collectionResourceRel = "location", path = "location")
public interface LocationRepo extends JpaRepository<Location,Long>,  QuerydslPredicateExecutor<Location>,QuerydslBinderCustomizer<QLocation>  {
	@Override
	default void customize(QuerydslBindings bindings, QLocation user) {
		bindings.bind(user.location).first((StringPath path, String value) -> {
				return path.containsIgnoreCase(value);
		});
	}
}
