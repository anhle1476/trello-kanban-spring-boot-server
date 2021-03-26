package com.codegym.kanban.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codegym.kanban.model.CardColumn;

public interface CardColumnRepository extends JpaRepository<CardColumn, Long> {

	@Query("SELECT MAX(c.columnOrder) FROM CardColumn c WHERE c.board.id = :boardId")
	Optional<Integer> getMaxColumnOrder(Long boardId);
}
