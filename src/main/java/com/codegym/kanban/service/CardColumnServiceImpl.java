package com.codegym.kanban.service;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegym.kanban.exception.BoardNotFoundException;
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
	public CardColumn updateColumn(Long userId, Long boardId, CardColumn cardColumn) {
		return null;
	}

	@Override
	public void updateColumnsOrder(Long userId, Long boardId, Map<Long, Long> orderMap) {

	}

	@Override
	public void disableColumn(Long userId, Long boardId, Long cardColumnId) {
		
	}

	@Override
	public void deleteColumn(Long userId, Long boardId, Long cardColumnId) {
		
	}
	
	private Board getCurrentBoard(Long userId, Long boardId) {
		return boardRepository.findByStatusEnabledIsTrueAndAppUserIdAndId(userId, boardId)
			.orElseThrow(() -> new BoardNotFoundException("Bảng " + boardId + " không có sẵn"));
	}

}
