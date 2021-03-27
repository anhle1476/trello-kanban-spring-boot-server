package com.codegym.kanban.exception;

public class CardColumnNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -4906103511372611915L;
	public static final Integer ERROR_CODE = 30;
	
	public CardColumnNotFoundException(String message) {
		super(message);
	}	
}
