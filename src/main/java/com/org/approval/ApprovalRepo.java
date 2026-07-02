package com.org.approval;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.StringPath;

@Repository
@PreAuthorize("isFullyAuthenticated()")
@RepositoryRestResource(collectionResourceRel = "approvals", path = "approvals")
public interface ApprovalRepo extends JpaRepository<Approval, Long>, QuerydslPredicateExecutor<Approval>,
		QuerydslBinderCustomizer<QApproval> {
	@Override
	default void customize(QuerydslBindings bindings, QApproval obj) {
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

	 
	}
}
