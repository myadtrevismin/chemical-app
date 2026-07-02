package com.core.blog;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.core.blog.repo.BlogRepo;

@Service
public class BlogService {
	@Autowired
	EntityManager em;

	@Autowired
	BlogRepo postRepo;

	@Transactional
	public void patchPost(long id) {
		em.createNativeQuery("update blog set active=1-active where id=:id").setParameter("id", id).executeUpdate();
	}

	@Transactional
	public void patchComment(long id, long cid) {
		em.createNativeQuery("update blog_comment set active=1-active where id=:cid and blog=:id")
		.setParameter("cid", cid).setParameter("id", id).executeUpdate();
	}
}
