package com.core.common;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

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
