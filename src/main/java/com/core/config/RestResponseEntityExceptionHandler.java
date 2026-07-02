package com.core.config;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.core.common.ErrorResponse;
import com.core.common.MyFieldError;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	ApplicationContext context;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		// ex.getBindingResult(): extract the bind result for default message.
		String errorResult = ex.getBindingResult().toString();

		return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ RepositoryConstraintViolationException.class })
	public ResponseEntity<ErrorResponse> handleFieldExceptions(Exception ex, WebRequest request) {
		RepositoryConstraintViolationException nevEx = (RepositoryConstraintViolationException) ex;

		ErrorResponse er = new ErrorResponse();

		try {
			nevEx.getMessage();
			List<FieldError> fieldErrors = nevEx.getErrors().getFieldErrors();
			for (FieldError s : fieldErrors) {
				MyFieldError mfe = new MyFieldError();
				mfe.setField(s.getField());
				mfe.setErrorMessage(context.getMessage(s.getCode(), null, Locale.ENGLISH));
				er.addFieldError(mfe);
			}

			List<ObjectError> oeList = nevEx.getErrors().getGlobalErrors();
			for (ObjectError s : oeList) {
				er.addGlobalErrors(context.getMessage(s.getCode(), null, Locale.ENGLISH));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<ErrorResponse>(er, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {

		if (ex instanceof ResourceNotFoundException) {
			ErrorResponse error = new ErrorResponse();
			error.setErrorCode(HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
		} else if (ex instanceof AccessDeniedException) {
			ErrorResponse error = new ErrorResponse();
			error.setErrorCode(HttpStatus.UNAUTHORIZED.value());
			return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
		} else if (ex instanceof ConstraintViolationException) {
			ErrorResponse error = new ErrorResponse();
			((ConstraintViolationException) ex).getCause();
			error.setErrorCode(HttpStatus.UNAUTHORIZED.value());
			return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
		} else if (ex instanceof TransactionSystemException) {
			ErrorResponse error = new ErrorResponse();
			Set<ConstraintViolation<?>> errors = ((ConstraintViolationException) (((TransactionSystemException) ex)
					.getCause()).getCause()).getConstraintViolations();
			for (ConstraintViolation<?> t : errors) {
				MyFieldError mfe = new MyFieldError();
				mfe.setField(t.getPropertyPath() + "");
				mfe.setErrorMessage(t.getMessage());
				error.addFieldError(mfe);
			}

			error.setErrorCode(HttpStatus.UNAUTHORIZED.value());

			return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
		} else if (ex instanceof DataIntegrityViolationException) {
			 
			ErrorResponse error = new ErrorResponse();
			String message=((DataIntegrityViolationException) ex).getRootCause().toString();
			message=message.substring(message.indexOf(":")+1);
			message=message.replaceAll(" key", "");
			message=message.replaceAll("_UNIQUE", "");
			error.addGlobalErrors(message);
			return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			ex.printStackTrace();

			ErrorResponse error = new ErrorResponse();

			error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

			error.addGlobalErrors(ex.getMessage());

			return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}