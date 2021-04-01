package com.codegym.kanban.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codegym.kanban.dto.BoardInfoDTO;
import com.codegym.kanban.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

	List<BoardInfoDTO> findByStatusEnabledIsTrueAndAppUserIdOrderByStatusUpdatedAtDesc(Long appUserId);
	
	List<BoardInfoDTO> findByStatusEnabledIsFalseAndAppUserIdOrderByStatusUpdatedAtDesc(Long appUserId);
	
	Optional<Board> findByStatusEnabledIsTrueAndAppUserIdAndId(Long appUserId, Long id);
	
	@Modifying
	@Query("UPDATE Board b SET b.status.enabled = FALSE "
			+ "WHERE b.appUser.id = :appUserId AND b.id = :id")
	Integer disableBoard(Long appUserId, Long id);
	
}
