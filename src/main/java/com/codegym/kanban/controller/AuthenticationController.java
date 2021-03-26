package com.codegym.kanban.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codegym.kanban.dto.AppUserDTO;
import com.codegym.kanban.model.AppUser;
import com.codegym.kanban.model.Role;
import com.codegym.kanban.service.AppUserService;
import com.codegym.kanban.utils.AppUtils;
import com.codegym.kanban.validator.PasswordValidator;

@RestController
public class AuthenticationController {

	@Autowired
	private AppUserService appUserService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordValidator passwordValidator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AppUtils validationErrorUtils;
	
	@PostMapping("/register")
	public ResponseEntity<?> doRegister(@Valid @RequestBody AppUser user, BindingResult result) {
		System.out.println("ok");
		if (result.hasErrors()) 
			return validationErrorUtils.mapErrorToResponse(result);
		
		System.out.println("ok valid");
		
		user.setRole(Role.ROLE_USER);
		String encoded = passwordEncoder.encode(user.getPassword());
		user.setPassword(encoded);
		AppUser savedUser = appUserService.saveUser(user);
		AppUserDTO dto = modelMapper.map(savedUser, AppUserDTO.class);
		return new ResponseEntity<AppUserDTO>(dto, HttpStatus.CREATED);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(passwordValidator);
	}
}
