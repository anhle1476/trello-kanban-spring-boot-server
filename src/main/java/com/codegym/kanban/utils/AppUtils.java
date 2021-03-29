package com.codegym.kanban.utils;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.codegym.kanban.exception.StateDisabledException;
import com.codegym.kanban.model.AppUser;
import com.codegym.kanban.model.Status;

@Component
public class AppUtils {

	public ResponseEntity<?> mapErrorToResponse(BindingResult result) {
		List<FieldError> fieldErrors = result.getFieldErrors();
		Map<String, String> errors = new HashMap<>();
		for (FieldError fieldError : fieldErrors) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
	}
	
	public AppUser extractUserInfoFromToken(Principal principal) {
		return (AppUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	}
	
	public void assertStatusIsDisabled(Status status) {
		if (!status.isEnabled()) 
			throw new StateDisabledException("Đối tượng đã bị vô hiệu hóa, không thể cập nhật");
	}
}
