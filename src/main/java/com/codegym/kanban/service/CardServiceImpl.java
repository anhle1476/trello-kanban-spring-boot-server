package com.codegym.kanban.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegym.kanban.exception.CardColumnNotFoundException;
import com.codegym.kanban.model.Card;
import com.codegym.kanban.model.CardColumn;
import com.codegym.kanban.repository.CardColumnRepository;
import com.codegym.kanban.repository.CardRepository;

@Service
public class CardServiceImpl implements CardService {
	@Autowired
	private CardColumnRepository cardColumnRepository;
	
	@Autowired
	private CardRepository cardRepository;

	@Override
	public Card saveCard(Long userId, Long boardId, Long cardColumnId, Card card) {
		CardColumn column = findColumn(userId, boardId, cardColumnId);
		Integer maxOrder = cardRepository.getMaxCardOrder(cardColumnId).orElse(-1);
		
		card.setCardColumn(column);
		card.setCardOrder(maxOrder + 1);
		
		return cardRepository.save(card);
	}

	

	@Override
	public Card updateCard(Long userId, Long boardId, Long cardColumnId, Card card) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCardsOrder(Long userId, Long boardId, Long cardColumnId, Map<Long, Long> orderMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableCard(Long userId, Long boardId, Long cardColumnId, Long cardId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCard(Long userId, Long boardId, Long cardColumnId, Long cardId) {
		// TODO Auto-generated method stub
		
	}
	
	private CardColumn findColumn(Long userId, Long boardId, Long cardColumnId) {
		 return cardColumnRepository.findAvailableColumn(userId, boardId, cardColumnId).orElseThrow(
					() -> new CardColumnNotFoundException("Cột " + cardColumnId + " không có sẵn"));
	}

}
