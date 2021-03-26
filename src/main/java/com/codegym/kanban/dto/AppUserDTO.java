package com.codegym.kanban.dto;

import lombok.Data;

@Data
public class AppUserDTO {
	private Long id;
	private String email;
	private String fullname;
	private String role;
}
