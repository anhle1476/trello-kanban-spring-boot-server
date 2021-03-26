package com.codegym.kanban.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class StateDisabledException extends RuntimeException {

	private static final long serialVersionUID = 2200974884076701522L;

	public static final Integer ERROR_CODE = 2;
	
	public StateDisabledException(String message) {
		super(message);
	}

	
}
