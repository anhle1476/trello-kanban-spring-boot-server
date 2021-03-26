package com.codegym.kanban.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codegym.kanban.model.AppUser;

@Component
public class PasswordValidator implements Validator  {
	
	private Pattern passwordPartern = Pattern.compile("^\\pL{8,20}$");;

	@Override
	public boolean supports(Class<?> clazz) {
		return AppUser.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		String password = ((AppUser) target).getPassword();
		
		if (password == null) {
			errors.rejectValue("password",null, "Mật khẩu không được để trống");
			return;
		}
		
		boolean isValid = passwordPartern.matcher(password).matches();
		if (!isValid) {
			errors.rejectValue("password",null, "Mật khẩu phải từ 8-20 ký tự và không có ký tự đặc biệt");
		}
	}

}
