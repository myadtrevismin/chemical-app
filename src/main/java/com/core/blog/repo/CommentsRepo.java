package com.core.blog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.core.blog.BlogComment;
import com.core.blog.QBlogComment;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@RepositoryRestResource(path = "comments", collectionResourceRel = "comments")
public interface CommentsRepo extends JpaRepository<BlogComment, Long>, QuerydslPredicateExecutor<BlogComment>,
		QuerydslBinderCustomizer<QBlogComment> {
	@Override
	default void customize(QuerydslBindings bindings, QBlogComment post) {
		bindings.bind(post.comment).first((StringPath path, String value) -> {
			if (value.startsWith("%") && value.endsWith("%")) {
				return path.containsIgnoreCase(value.substring(1, value.length() - 1));
			} else {
				return path.eq(value);
			}
		});
	}
}
