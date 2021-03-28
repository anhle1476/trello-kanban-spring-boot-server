package com.codegym.kanban.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codegym.kanban.model.CardColumn;

public interface CardColumnRepository extends JpaRepository<CardColumn, Long> {

	@Query("SELECT MAX(c.columnOrder) FROM CardColumn c WHERE c.board.id = :boardId")
	Optional<Integer> getMaxColumnOrder(Long boardId);
	
	@Query("SELECT c FROM CardColumn c "
			+ "WHERE c.id = :id "
			+ "AND c.board.appUser.id = :userId")
	Optional<CardColumn> findUserColumnById(Long userId, Long id);
	
	@Modifying
	@Query("UPDATE CardColumn c SET c.title = :title "
			+ "WHERE c.id = :id "
			+ "AND c.board.appUser.id = :userId")
	Integer updateColumnTitle(Long userId, Long id, String title);
}
