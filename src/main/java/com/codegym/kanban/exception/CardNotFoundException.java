package com.codegym.kanban.exception;

public class CardNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -2646581923551096416L;
	public static final Integer ERROR_CODE = 40;
	
	public CardNotFoundException(String message) {
		super(message);
	}	
}
