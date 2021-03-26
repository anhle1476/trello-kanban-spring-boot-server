package com.codegym.kanban.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AppUser implements UserDetails {

	private static final long serialVersionUID = 1304449756986389724L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Email(message = "Địa chỉ email không hợp lệ")
	@NotBlank(message = "Email không được để trống")
	@Column(unique = true, nullable = false, updatable = false)
	private String email;
	
	@Size(min = 4, max = 25, message = "Tên người dùng phải chứa từ 4-25 ký tự")
	@NotBlank(message = "Tên người dùng không được để trống")
	private String fullname;
	
	@Column(length = 255)
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Embedded
	private Status status;
	
	@OneToMany(mappedBy = "appUser")
	private List<Board> boards; 
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority(this.role.name()));
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.status.isEnabled();
	}

	@Override
	public String toString() {
		return "AppUser [id=" + id + ", email=" + email + ", fullname=" + fullname + ", password=" + password
				+ ", role=" + role + ", status=" + status + "]";
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
