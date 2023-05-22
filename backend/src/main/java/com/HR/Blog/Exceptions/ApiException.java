package com.HR.Blog.Exceptions;

public class ApiException extends RuntimeException {

	public ApiException(String message) {
		super(message);
	}

	public ApiException() {
		super();
	}

}
