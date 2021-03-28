package com.codegym.kanban.service;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegym.kanban.exception.BoardNotFoundException;
import com.codegym.kanban.exception.CardColumnNotFoundException;
import com.codegym.kanban.model.Board;
import com.codegym.kanban.model.CardColumn;
import com.codegym.kanban.repository.BoardRepository;
import com.codegym.kanban.repository.CardColumnRepository;

@Service
public class CardColumnServiceImpl implements CardColumnService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private CardColumnRepository cardColumnRepository;

	@Override
	@Transactional
	public CardColumn saveColumn(Long userId, Long boardId, CardColumn cardColumn) {
		Board board = getCurrentBoard(userId, boardId);
		Integer maxOrder = cardColumnRepository.getMaxColumnOrder(boardId).orElse(-1);
		cardColumn.setBoard(board);
		cardColumn.setColumnOrder(maxOrder + 1);
		
		return cardColumnRepository.save(cardColumn);
	}

	

	@Override
	public CardColumn updateColumn(Long userId, CardColumn cardColumn) {
		CardColumn found = cardColumnRepository.findAvailableColumn(userId, cardColumn.getId())
				.orElseThrow(() -> getColumnNotFoundException(cardColumn.getId()));
		found.setTitle(cardColumn.getTitle());
		return cardColumnRepository.save(found);
	}

	@Override
	public void updateColumnsOrder(Long userId, Long boardId, Map<Long, Long> orderMap) {

	}

	@Override
	public void disableColumn(Long userId, Long cardColumnId) {
		Integer affected = cardColumnRepository.disableColumn(userId, cardColumnId);
		if (affected == 0)
			throw getColumnNotFoundException(cardColumnId);
	}

	@Override
	public void deleteColumn(Long userId, Long cardColumnId) {
		Integer affected = cardColumnRepository.deleteColumn(userId, cardColumnId);
		if (affected == 0)
			throw getColumnNotFoundException(cardColumnId);
	}
	
	private Board getCurrentBoard(Long userId, Long boardId) {
		return boardRepository.findByStatusEnabledIsTrueAndAppUserIdAndId(userId, boardId)
			.orElseThrow(() -> new BoardNotFoundException("Bảng " + boardId + " không có sẵn"));
	}
	
	private CardColumnNotFoundException getColumnNotFoundException(Long colId) {
		return new CardColumnNotFoundException("Cột " + colId + " không có sẵn");
	}

}
