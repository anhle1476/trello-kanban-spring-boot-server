package com.codegym.kanban.dto;

import com.codegym.kanban.model.Status;

import lombok.Data;

@Data
public class CardColumnInfoDTO {
	private Long id;

	private String title;

	private Integer columnOrder;

	private Status status;
}
