package com.core.user.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.core.user.entity.QUser;
import com.core.user.entity.User;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@RepositoryRestResource(path = "users")
public interface UserRepo
		extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {
	Optional<User> findByEmail(String email);

	@PreAuthorize("isFullyAuthenticated()")
	@Query("select t from User t where (t.role = :role or 'ALL'=:role) and  (UPPER(t.name) like :name% or UPPER(t.lname) like :name%) ")
	List<User> searchByNameAndType(@Param("name") String name, @Param("role") String role);

	@Query("select t.id from User t where t.role = 'ROLE_ADMIN'")
	@Secured("ROLE_ADMIN")
	List<Long> findAdminIds();

	@RestResource(exported = false)
	void deleteById(Long id);

	@Override
	default void customize(QuerydslBindings bindings, QUser user) {
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
		bindings.bind(user.name).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}

}
