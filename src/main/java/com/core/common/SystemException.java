package com.core.common;

import java.util.List;


@SuppressWarnings("serial")
public class SystemException extends GenericRuntimeException {
	public SystemException() {
		super();
	}


	private List<String> errorList;

	public SystemException(String string) {
		super(string);
	}

	public SystemException(Throwable throwable) {
		super(throwable);
	}

	public SystemException(String string, Throwable throwable) {
		super(string, throwable);
	}

	  
	public SystemException(String string, List<String> errorList) {
		super(string);
		this.errorList = errorList;
	}

	 

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}
}
