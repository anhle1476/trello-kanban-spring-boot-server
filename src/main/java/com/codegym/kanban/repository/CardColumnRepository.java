package com.codegym.kanban.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codegym.kanban.model.CardColumn;

public interface CardColumnRepository extends JpaRepository<CardColumn, Long> {

	@Query("SELECT MAX(c.columnOrder) FROM CardColumn c WHERE c.board.id = :boardId")
	Optional<Integer> getMaxColumnOrder(Long boardId);
	
	@Query("SELECT c FROM CardColumn c "
			+ "WHERE c.id = :id "
			+ "AND c.status.enabled = TRUE "
			+ "AND c.board.id = :boardId "
			+ "AND c.board.appUser.id = :userId")
	Optional<CardColumn> findAvailableColumn(Long userId, Long boardId, Long id);
	
}
