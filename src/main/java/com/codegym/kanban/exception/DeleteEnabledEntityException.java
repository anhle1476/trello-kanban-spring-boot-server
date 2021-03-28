package com.codegym.kanban.exception;

public class DeleteEnabledEntityException extends RuntimeException {
	
	private static final long serialVersionUID = 418994479020810031L;
	public static final Integer ERROR_CODE = 3;
	
	public DeleteEnabledEntityException(String message) {
		super(message);
	}
}
