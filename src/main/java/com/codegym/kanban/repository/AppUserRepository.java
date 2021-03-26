package com.codegym.kanban.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codegym.kanban.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByEmail(String email);
	
	@Modifying
	@Query("UPDATE AppUser a SET a.status.enabled = FALSE WHERE a.id = :id")
	Integer disableUserById(@Param("id") int id);
	
	@Modifying
	@Query("UPDATE AppUser a SET a.status.enabled = TRUE WHERE a.id = :id")
	Integer enableUserById(@Param("id") int id);
	
	List<AppUser> findByStatusEnabledIsTrue();
}
