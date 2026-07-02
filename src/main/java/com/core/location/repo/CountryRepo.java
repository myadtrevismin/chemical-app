package com.core.location.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.core.location.Country;
import com.core.location.QCountry;
import com.querydsl.core.types.dsl.StringPath;


@Repository
@RepositoryRestResource(collectionResourceRel = "countries", path = "countries")
public interface CountryRepo
		extends JpaRepository<Country, Long>, QuerydslPredicateExecutor<Country>, QuerydslBinderCustomizer<QCountry> {
	@Override
	default void customize(QuerydslBindings bindings, QCountry obj) {

		bindings.bind(obj.name).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}

}
