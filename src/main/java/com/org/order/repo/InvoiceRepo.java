package com.org.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.org.order.InvoiceHistory;
import com.org.order.QInvoiceHistory;

@Repository
@RepositoryRestResource(collectionResourceRel = "invoice-history", path = "invoice-history")
public interface InvoiceRepo extends JpaRepository<InvoiceHistory, Long>, QuerydslPredicateExecutor<InvoiceHistory>, QuerydslBinderCustomizer<QInvoiceHistory>{
	@Override
	default void customize(QuerydslBindings bindings, QInvoiceHistory obj) {
		
	}
}
