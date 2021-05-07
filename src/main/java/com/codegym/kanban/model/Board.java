package com.codegym.kanban.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Tên bảng không được để trống")
	@Size(min = 4, max = 25, message = "Tên bảng phải chứa từ 4-25 ký tự")
	@Column(nullable = false)
	private String title;
	

	@Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Mã màu không hợp lệ")
	private String color;
	
	private LocalDateTime lastedView;
	
	@Embedded
	private Status status;
	
	@ManyToOne
	@JsonIgnore
	private AppUser appUser;
	
	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<CardColumn> cardColumns;

	@Override
	public String toString() {
		return "Board [id=" + id + ", title=" + title + ", color=" + color + ", lastedView=" + lastedView + ", status="
				+ status + "]";
	}
	
	
	@PrePersist
	protected void onPersist() {
		this.status = new Status();
		this.status.setCreatedAt(LocalDateTime.now());
		this.lastedView = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.status.setUpdatedAt(LocalDateTime.now());
	}

}
