package com.codegym.kanban.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2534998734272086120L;

	public static final Integer ERROR_CODE = 10;

	public UserNotFoundException(String message) {
		super(message);
	}

}
