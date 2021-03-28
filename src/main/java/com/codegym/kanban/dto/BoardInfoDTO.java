package com.codegym.kanban.dto;

import java.time.LocalDateTime;

import com.codegym.kanban.model.Status;

public interface BoardInfoDTO {

	Long getId();

	String getTitle();

	String getColor();

	Status getStatus();
	
	LocalDateTime getLastedView();
}
