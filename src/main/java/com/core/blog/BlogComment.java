package com.core.blog;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity(name = "blog_comment")
@Table(name = "blog_comment")
public class BlogComment implements Serializable {
	@SequenceGenerator(name = "postSEQ1", sequenceName = "id_sequence", allocationSize = 5)
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postSEQ1")
	private Long id;
	
	private String comment;
	
	private String name;
	
	private int level;
	
	private Long blog;
	
	private boolean active;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="parent")
	private BlogComment parent; 
	
	@OneToMany(mappedBy = "parent")
	private List<BlogComment> childComments;
	
	@Column(name = "creation_date", nullable = false, unique = false)
	private Date creationDate;
	
	@PrePersist
	public void prePerist() {
		this.active = true;
		this.creationDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Long getPost() {
		return blog;
	}

	public void setPost(Long post) {
		this.blog = post;
	}

	public List<BlogComment> getChildComments() {
		return childComments;
	}

	public void setChildComments(List<BlogComment> childComments) {
		this.childComments = childComments;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public BlogComment getParent() {
		return parent;
	}

	public void setParent(BlogComment parent) {
		this.parent = parent;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
	
}
