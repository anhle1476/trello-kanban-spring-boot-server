package com.codegym.kanban.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(min = 1, max = 255, message = "Tiêu đề thẻ phải chứa từ 1-255 ký tự")
	@Column(nullable = false)
	private String title;
	
	@Column(length = 10000)
	private String details;
	
	@PositiveOrZero(message = "Thứ tự thẻ không hợp lệ")
	private Integer cardOrder;

	@Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Mã màu không hợp lệ")
	private String label;
	
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate startDate;
	
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dueDate;
	
	@Embedded
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private CardColumn cardColumn;
	
	@PrePersist
	protected void onPersist() {
		this.status = new Status();
		this.status.setCreatedAt(LocalDateTime.now());
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.status.setUpdatedAt(LocalDateTime.now());
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", title=" + title + ", details=" + details + ", cardOrder=" + cardOrder + ", label="
				+ label + ", startDate=" + startDate + ", dueDate=" + dueDate + ", status=" + status + "]";
	}
	
}
