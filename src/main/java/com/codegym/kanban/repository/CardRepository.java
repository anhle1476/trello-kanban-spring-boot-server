package com.codegym.kanban.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codegym.kanban.model.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
	@Query("SELECT MAX(c.cardOrder) FROM Card c WHERE c.cardColumn.id = :columnId")
	Optional<Integer> getMaxCardOrder(Long columnId);
}
