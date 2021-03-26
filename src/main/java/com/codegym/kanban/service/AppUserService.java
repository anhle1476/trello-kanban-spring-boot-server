package com.codegym.kanban.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.codegym.kanban.model.AppUser;

public interface AppUserService extends UserDetailsService {
	AppUser saveUser(AppUser appUser);
	
	void disableUser(int id); 

	void enableUser(int id);
}
