package com.codegym.kanban.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

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
	
	@Size(min = 1, max = 30, message = "Tiêu đề thẻ phải chứa từ 1-30 ký tự")
	@Column(nullable = false)
	private String title;
	
	@SafeHtml(whitelistType = WhiteListType.RELAXED)
	@Column(length = 10000)
	private String details;
	
	@PositiveOrZero(message = "Thứ tự thẻ không hợp lệ")
	private Integer cardOrder;
	
	@Enumerated(EnumType.STRING)
	private Label label;
	
	@Embedded
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private CardColumn cardColumn;

	@Override
	public String toString() {
		return "Card [id=" + id + ", title=" + title + ", details=" + details + ", cardOrder=" + cardOrder + ", label="
				+ label + ", status=" + status + "]";
	}
	
	@PrePersist
	protected void onPersist() {
		this.status = new Status();
		this.status.setCreatedAt(LocalDateTime.now());
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.status.setUpdatedAt(LocalDateTime.now());
	}
	
}
