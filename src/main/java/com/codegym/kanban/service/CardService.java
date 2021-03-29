package com.codegym.kanban.service;

import java.util.Map;

import com.codegym.kanban.model.Card;

public interface CardService {
	Card saveCard(Long userId, Long cardColumnId, Card card);
	
	Card updateCard(Long userId, Card card);
	
	void updateCardsOrder(Long userId, Map<Long, Long> orderMap);
	
	void disableCard(Long userId, Long cardId);
	
	void deleteCard(Long userId, Long cardId);
	
	Card getUserCardById(Long userId, Long cardId);
}
