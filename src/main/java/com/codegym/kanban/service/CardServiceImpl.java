package com.codegym.kanban.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegym.kanban.exception.CardColumnNotFoundException;
import com.codegym.kanban.exception.CardNotFoundException;
import com.codegym.kanban.exception.DeleteEnabledEntityException;
import com.codegym.kanban.model.Card;
import com.codegym.kanban.model.CardColumn;
import com.codegym.kanban.repository.CardColumnRepository;
import com.codegym.kanban.repository.CardRepository;
import com.codegym.kanban.utils.AppUtils;

@Service
public class CardServiceImpl implements CardService {
	@Autowired
	private CardColumnRepository cardColumnRepository;
	
	@Autowired
	private CardRepository cardRepository;
	
	@Autowired
	private AppUtils appUtils;

	@Override
	public Card saveCard(Long userId, Long cardColumnId, Card card) {
		CardColumn column = findColumn(userId, cardColumnId);
		Integer maxOrder = cardRepository.getMaxCardOrder(cardColumnId).orElse(-1);
		
		card.setCardColumn(column);
		card.setCardOrder(maxOrder + 1);
		
		return cardRepository.save(card);
	}

	

	@Override
	public Card updateCard(Long userId, Card card) {
		Card found = getUserCardById(userId, card.getId());
		appUtils.assertStatusIsDisabled(found.getStatus());
		
		found.setTitle(card.getTitle());
		found.setDetails(card.getDetails());
		found.setLabel(card.getLabel());
		found.setStartDate(card.getStartDate());
		found.setDueDate(card.getDueDate());
		
		return cardRepository.save(found);
	}

	@Override
	public void updateCardsOrder(Long userId, Map<Long, Long> orderMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableCard(Long userId, Long cardId) {
		Card found = getUserCardById(userId, cardId);
		appUtils.assertStatusIsDisabled(found.getStatus());
		found.getStatus().setEnabled(false);		
	}

	@Override
	public void deleteCard(Long userId, Long cardId) {
		Card found = getUserCardById(userId, cardId);
		if (found.getStatus().isEnabled())
			throw new DeleteEnabledEntityException("Không thể xóa thẻ chưa vô hiệu hóa");
		cardRepository.delete(found);
		
	}

	@Override
	public Card getUserCardById(Long userId, Long cardId) {
		return cardRepository.getUserCardById(userId, cardId).orElseThrow(
				() -> new CardNotFoundException("Thẻ " + cardId + " không tồn tại"));
	}

	private CardColumn findColumn(Long userId,Long cardColumnId) {
		 return cardColumnRepository.findUserColumnById(userId, cardColumnId).orElseThrow(
					() -> new CardColumnNotFoundException("Cột " + cardColumnId + " không có sẵn"));
	}
}
