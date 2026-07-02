package com.org.sales.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.org.sales.QSalesProduct;
import com.org.sales.SalesProduct;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@PreAuthorize("isFullyAuthenticated()")
@RepositoryRestResource(collectionResourceRel = "sales-products", path = "sales-products")
public interface SalesProductRepo extends JpaRepository<SalesProduct, Long>, QuerydslPredicateExecutor<SalesProduct>,
		QuerydslBinderCustomizer<QSalesProduct> {
	@Override
	default void customize(QuerydslBindings bindings, QSalesProduct obj) {
		bindings.bind(obj.product.code).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}
}
