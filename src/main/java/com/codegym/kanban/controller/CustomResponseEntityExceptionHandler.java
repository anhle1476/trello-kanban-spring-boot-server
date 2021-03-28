package com.codegym.kanban.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.codegym.kanban.dto.GeneralExceptionResponseDTO;
import com.codegym.kanban.exception.BadRequestFieldException;
import com.codegym.kanban.exception.BoardNotFoundException;
import com.codegym.kanban.exception.CardColumnNotFoundException;
import com.codegym.kanban.exception.DeleteEnabledEntityException;
import com.codegym.kanban.exception.StateDisabledException;
import com.codegym.kanban.exception.UserNotFoundException;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = {UserNotFoundException.class})
	private ResponseEntity<GeneralExceptionResponseDTO> handleUserNotFoundException(
			UserNotFoundException ex, WebRequest request) {
		return responseToGeneralExceptions(ex, UserNotFoundException.ERROR_CODE);
	}
	
	@ExceptionHandler(value = {BoardNotFoundException.class})
	private ResponseEntity<GeneralExceptionResponseDTO> handleBoardNotFoundException(
			BoardNotFoundException ex, WebRequest request) {
		return responseToGeneralExceptions(ex, BoardNotFoundException.ERROR_CODE);
	}
	
	@ExceptionHandler(value = {StateDisabledException.class})
	private ResponseEntity<GeneralExceptionResponseDTO> handleStateDisabledException(
			StateDisabledException ex, WebRequest request) {
		return responseToGeneralExceptions(ex, StateDisabledException.ERROR_CODE);
	}
	
	@ExceptionHandler(value = {CardColumnNotFoundException.class})
	private ResponseEntity<GeneralExceptionResponseDTO> handleCardColumnNotFoundException(
			CardColumnNotFoundException ex, WebRequest request) {
		return responseToGeneralExceptions(ex, CardColumnNotFoundException.ERROR_CODE);
	}
	
	@ExceptionHandler(value = {BadRequestFieldException.class})
	private ResponseEntity<GeneralExceptionResponseDTO> handleBadRequestFieldException(
			BadRequestFieldException ex, WebRequest request) {
		return responseToGeneralExceptions(ex, BadRequestFieldException.ERROR_CODE);
	}
	
	@ExceptionHandler(value = {DeleteEnabledEntityException.class})
	private ResponseEntity<GeneralExceptionResponseDTO> handleDeleteEnabledEntityException(
			DeleteEnabledEntityException ex, WebRequest request) {
		return responseToGeneralExceptions(ex, DeleteEnabledEntityException.ERROR_CODE);
	}

	private ResponseEntity<GeneralExceptionResponseDTO> responseToGeneralExceptions(RuntimeException ex, Integer code) {
		GeneralExceptionResponseDTO response = new GeneralExceptionResponseDTO(ex.getMessage(), code);
		return new ResponseEntity<GeneralExceptionResponseDTO>(response, HttpStatus.BAD_REQUEST);
	}

}
