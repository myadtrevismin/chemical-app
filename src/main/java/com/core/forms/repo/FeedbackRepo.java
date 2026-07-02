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

import com.core.forms.entity.Feedback;
import com.core.forms.entity.QFeedback;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@RepositoryRestResource(path = "feedbacks")
@Secured({"ROLE_ADMIN"})
public interface FeedbackRepo extends JpaRepository<Feedback, Long>, QuerydslPredicateExecutor<Feedback>, QuerydslBinderCustomizer<QFeedback> {

	Optional<Feedback> findById(Long id);

	@PostFilter("hasRole('ROLE_ADMIN') or filterObject.user.id == authentication.id")
	void deleteById(Long id);

	@PostFilter("hasRole('ROLE_ADMIN') or filterObject.user.id == authentication.id")
	List<Feedback> findAll();

	@Override
	default void customize(QuerydslBindings bindings, QFeedback user) {
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
