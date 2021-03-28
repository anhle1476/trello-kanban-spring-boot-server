package com.codegym.kanban.service;

import java.util.Map;

import com.codegym.kanban.model.Card;

public interface CardService {
	Card saveCard(Long userId, Long cardColumnId, Card card);
	
	Card updateCard(Long userId, Long cardColumnId, Card card);
	
	void updateCardsOrder(Long userId, Long cardColumnId, Map<Long, Long> orderMap);
	
	void disableCard(Long userId, Long cardId);
	
	void deleteCard(Long userId, Long cardId);
}
