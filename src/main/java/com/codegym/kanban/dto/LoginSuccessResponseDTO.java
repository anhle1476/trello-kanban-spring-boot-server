package com.codegym.kanban.dto;

import lombok.Data;

@Data
public class LoginSuccessResponseDTO {
	private String token;

	public LoginSuccessResponseDTO(String token) {
		this.token = token;
	}
}
