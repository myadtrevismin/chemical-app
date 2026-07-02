package com.org.product.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.org.product.ProductFlow;
import com.org.product.QProductFlow;

@Repository
@PreAuthorize("isFullyAuthenticated()")
@RepositoryRestResource(collectionResourceRel = "product-flow", path = "product-flow")
public interface ProductFlowRepo
		extends JpaRepository<ProductFlow, Long>, QuerydslPredicateExecutor<ProductFlow>, QuerydslBinderCustomizer<QProductFlow> {
	
	List<ProductFlow> findByReferenceAndRefType(Long reference,String refType);
	
	@Override
	default void customize(QuerydslBindings bindings, QProductFlow obj) {
		 
	}
}
