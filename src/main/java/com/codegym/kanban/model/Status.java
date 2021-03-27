package com.codegym.kanban.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Embeddable
@Data
public class Status {
	@Column(updatable = false)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime createdAt;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime updatedAt;
	private boolean enabled = true;
	
}
