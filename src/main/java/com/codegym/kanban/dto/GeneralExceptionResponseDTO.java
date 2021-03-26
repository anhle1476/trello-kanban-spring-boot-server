package com.codegym.kanban.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneralExceptionResponseDTO {
	
	private String message;
	private Integer code;

}
