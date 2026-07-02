package com.core.blog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.core.blog.Blog;
import com.core.blog.QBlog;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@RepositoryRestResource(path = "blog")
public interface BlogRepo
		extends JpaRepository<Blog, Long>, QuerydslPredicateExecutor<Blog>, QuerydslBinderCustomizer<QBlog> {
	@Override
	default void customize(QuerydslBindings bindings, QBlog blog) {
		bindings.bind(blog.title).first((StringPath path, String value) -> {

			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}
}
