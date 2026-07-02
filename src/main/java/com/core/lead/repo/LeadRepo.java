package com.core.lead.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.core.lead.Lead;
import com.core.lead.QLead;
import com.org.order.QOrder;

@Repository
@RepositoryRestResource(collectionResourceRel = "leads", path = "leads")
public interface LeadRepo extends JpaRepository<Lead, Long>, QuerydslPredicateExecutor<Lead>, QuerydslBinderCustomizer<QLead>{
	@Override
	default void customize(QuerydslBindings bindings, QLead obj) {
		
	}
}
