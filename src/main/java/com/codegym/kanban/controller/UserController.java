package com.codegym.kanban.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codegym.kanban.model.AppUser;
import com.codegym.kanban.utils.AppUtils;

@RestController
@RequestMapping("api/v1/")
public class UserController {
	
	@Autowired
	private AppUtils appUtils;
	
	@GetMapping("test") 
	public ResponseEntity<?> testJwt(Principal principal) {
		AppUser appUser = appUtils.extractUserInfoFromToken(principal);
		return new ResponseEntity<Long>(appUser.getId(), HttpStatus.OK);
	}

	
}
