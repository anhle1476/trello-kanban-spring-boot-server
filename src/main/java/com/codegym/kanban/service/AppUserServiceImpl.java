package com.codegym.kanban.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codegym.kanban.exception.UserNotFoundException;
import com.codegym.kanban.model.AppUser;
import com.codegym.kanban.repository.AppUserRepository;

@Service
public class AppUserServiceImpl implements AppUserService {

	@Autowired
	private AppUserRepository appUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return appUserRepository.findByEmail(email).orElseThrow(() -> 
			new UsernameNotFoundException("Email " + email + " không tồn tại"));
	}
	
	@Override
	@Transactional
	public AppUser saveUser(AppUser appUser) {
		return appUserRepository.save(appUser);
	}

	@Override
	@Transactional
	public void disableUser(int id) {
		Integer affected = appUserRepository.disableUserById(id);
		assertUpdateSuccess(id, affected);
	}

	@Override
	@Transactional
	public void enableUser(int id) {
		Integer affected = appUserRepository.enableUserById(id);
		assertUpdateSuccess(id, affected);
	}
	
	private void assertUpdateSuccess(int userId, Integer affected) {
		if (affected != 1)
			throw new UserNotFoundException("User ID " + userId + " not found");
	}

}
