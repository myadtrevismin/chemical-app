package com.org.sales.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.org.sales.QSalesQuotationProduct;
import com.org.sales.SalesQuotationProduct;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@PreAuthorize("isFullyAuthenticated()")
@RepositoryRestResource(collectionResourceRel = "sales-quotation-products", path = "sales-quotation-products")
public interface SalesQuotationProductRepo extends JpaRepository<SalesQuotationProduct, Long>,
		QuerydslPredicateExecutor<QSalesQuotationProduct>, QuerydslBinderCustomizer<QSalesQuotationProduct> {
	@Override
	default void customize(QuerydslBindings bindings, QSalesQuotationProduct obj) {
		bindings.bind(obj.product.code).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}
}
