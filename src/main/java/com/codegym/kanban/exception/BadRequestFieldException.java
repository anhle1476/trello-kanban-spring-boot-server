package com.codegym.kanban.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestFieldException extends RuntimeException {

	private static final long serialVersionUID = 4322681847252320992L;

	public static final Integer ERROR_CODE = 1;
	
	public BadRequestFieldException(String message) {
		super(message);
	}

	
}
