package com.codegym.kanban.service;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegym.kanban.exception.BoardNotFoundException;
import com.codegym.kanban.exception.CardColumnNotFoundException;
import com.codegym.kanban.exception.DeleteEnabledEntityException;
import com.codegym.kanban.exception.StateDisabledException;
import com.codegym.kanban.model.Board;
import com.codegym.kanban.model.CardColumn;
import com.codegym.kanban.model.Status;
import com.codegym.kanban.repository.BoardRepository;
import com.codegym.kanban.repository.CardColumnRepository;

@Service
public class CardColumnServiceImpl implements CardColumnService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private CardColumnRepository cardColumnRepository;
	
	@Override
	public CardColumn findById(Long userId, Long columnId) {
		return cardColumnRepository.findAvailableColumn(userId, columnId)
				.orElseThrow(() -> getColumnNotFoundException(columnId));
	}

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
	@Transactional
	public CardColumn updateColumn(Long userId, CardColumn cardColumn) {
		CardColumn found = findById(userId, cardColumn.getId());
		found.setTitle(cardColumn.getTitle());
		return cardColumnRepository.save(found);
	}

	@Override
	@Transactional
	public void updateColumnsOrder(Long userId, Long boardId, Map<Long, Long> orderMap) {

	}

	@Override
	@Transactional
	public void disableColumn(Long userId, Long cardColumnId) {
		CardColumn column = findById(userId, cardColumnId);
		Status status = column.getStatus();
		if (!status.isEnabled()) 
			throw new StateDisabledException("Cột đã bị vô hiệu hóa sẵn");
		status.setEnabled(false);
		cardColumnRepository.save(column);
	}

	@Override
	@Transactional
	public void deleteColumn(Long userId, Long cardColumnId) {
		CardColumn column = findById(userId, cardColumnId);
		Status status = column.getStatus();
		if (status.isEnabled()) 
			throw new DeleteEnabledEntityException("Không thể xóa cột chưa vô hiệu hóa");
		cardColumnRepository.delete(column);
	}
	
	private Board getCurrentBoard(Long userId, Long boardId) {
		return boardRepository.findByStatusEnabledIsTrueAndAppUserIdAndId(userId, boardId)
			.orElseThrow(() -> new BoardNotFoundException("Bảng " + boardId + " không có sẵn"));
	}
	
	private CardColumnNotFoundException getColumnNotFoundException(Long colId) {
		return new CardColumnNotFoundException("Cột " + colId + " không có sẵn");
	}

}
