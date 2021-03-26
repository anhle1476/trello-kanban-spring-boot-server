package com.codegym.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegym.kanban.model.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

}
