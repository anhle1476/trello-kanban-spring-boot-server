package com.codegym.kanban.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BoardNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3039360806380652261L;
	
	public static final Integer ERROR_CODE = 20;

	public BoardNotFoundException(String message) {
		super(message);
	}
	
}
