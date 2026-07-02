package com.core.trans;

import java.util.List;

public class CollectionEntity<T> {
	private List<T> list;
	
	private long totalResults;
	
	public CollectionEntity() {}

	public CollectionEntity(List<T> list, Long totalResults) {
		this.list = list;
		this.totalResults = totalResults;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> users) {
		this.list = users;
	}

	public long getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}
}
