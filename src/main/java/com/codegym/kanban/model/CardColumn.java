package com.codegym.kanban.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.PositiveOrZero;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CardColumn {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@PositiveOrZero(message = "Thứ tự cột không hợp lệ")
	private Integer columnOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	private Board board;
	
	@Embedded
	private Status status;
	
	@OneToMany(mappedBy = "cardColumn", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Card> cards;

	@Override
	public String toString() {
		return "CardColumn [id=" + id + ", title=" + title + ", order=" + columnOrder + ", status=" + status + "]";
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
