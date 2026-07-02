package com.org.purchase.projection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.org.purchase.PurchaseProduct;
import com.org.purchase.QPurchaseProduct;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@PreAuthorize("isFullyAuthenticated()")
@RepositoryRestResource(collectionResourceRel = "purchases-products", path = "purchases-products")
public interface PurchaseProductRepo extends JpaRepository<PurchaseProduct, Long>,
		QuerydslPredicateExecutor<PurchaseProduct>, QuerydslBinderCustomizer<QPurchaseProduct> {
	@Override
	default void customize(QuerydslBindings bindings, QPurchaseProduct obj) {
		bindings.bind(obj.product.code).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}
}
