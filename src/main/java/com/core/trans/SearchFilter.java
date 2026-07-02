package com.core.trans;

import com.core.common.CommonUtils;

public class SearchFilter extends CollectionFilter {

	private String searchKey;

	public SearchFilter() {
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		if (CommonUtils.isNotNullOrEmpty(searchKey)) {
			this.searchKey = searchKey; // CommonUtilsX.decode();
		}
	}
}
