package com.core.common;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
	private int errorCode;

	private List<String> globalErrors = new ArrayList<String>();

	private List<MyFieldError> fieldError = new ArrayList<MyFieldError>();

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public void addFieldError(MyFieldError fe) {
		fieldError.add(fe);
	}

	public void addGlobalErrors(String error) {
		globalErrors.add(error);
	}

	public List<String> getGlobalErrors() {
		return globalErrors;
	}

	public void setGlobalErrors(List<String> globalErrors) {
		this.globalErrors = globalErrors;
	}

	public List<MyFieldError> getFieldError() {
		return fieldError;
	}

	public void setFieldError(List<MyFieldError> fieldError) {
		this.fieldError = fieldError;
	}

}
