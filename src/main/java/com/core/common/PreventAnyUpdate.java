package com.core.common;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class PreventAnyUpdate {

	@PrePersist
	void onPrePersist(Object o) {
		throw new RuntimeException("...");
	}

	@PreUpdate
	void onPreUpdate(Object o) {
		throw new RuntimeException("...");
	}

	@PreRemove
	void onPreRemove(Object o) {
		throw new RuntimeException("...");
	}
}
