package com.core.forms.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Repository;

import com.core.forms.entity.EmailSubscription;
import com.core.forms.entity.QEmailSubscription;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@RepositoryRestResource(path = "subscribers", collectionResourceRel = "subscribers")
@Secured({ "ROLE_ADMIN" })
public interface SubscribersRepo extends JpaRepository<EmailSubscription, Long>,
		QuerydslPredicateExecutor<EmailSubscription>, QuerydslBinderCustomizer<QEmailSubscription> {

	Optional<EmailSubscription> findById(Long id);

	@PostFilter("hasRole('ROLE_ADMIN') or filterObject.uid == authentication.id")
	void deleteById(Long id);

	@PostFilter("hasRole('ROLE_ADMIN') or filterObject.uid == authentication.id")
	List<EmailSubscription> findAll();

	@Override
	default void customize(QuerydslBindings bindings, QEmailSubscription user) {
		bindings.bind(user.email).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
		bindings.bind(user.creationDate).all((path, value) -> {
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
