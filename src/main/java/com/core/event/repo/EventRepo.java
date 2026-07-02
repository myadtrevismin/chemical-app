package com.core.event.repo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.core.event.Event;
import com.core.event.QEvent;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@RepositoryRestResource(path = "events")
public interface EventRepo  extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event>, QuerydslBinderCustomizer<QEvent>{
	default void customize(QuerydslBindings bindings, QEvent obj) {
		bindings.bind(obj.fromTime).all((path, value) -> {
			List<? extends Date> dates = new ArrayList<>(value);
			if (dates.size() == 1) {
				return Optional.of(path.after(dates.get(0)));
			} else {
				Date from = dates.get(0);
				Date to = dates.get(1);
				return Optional.of(path.between(from, to));
			}
		});
		bindings.bind(obj.title).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}
	
	Optional<Event> findByTypeAndParent(String type, Long parent);
}

