package com.codegym.kanban.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegym.kanban.exception.BoardNotFoundException;
import com.codegym.kanban.exception.CardColumnNotFoundException;
import com.codegym.kanban.exception.DeleteEnabledEntityException;
import com.codegym.kanban.model.Board;
import com.codegym.kanban.model.CardColumn;
import com.codegym.kanban.model.Status;
import com.codegym.kanban.repository.BoardRepository;
import com.codegym.kanban.repository.CardColumnRepository;
import com.codegym.kanban.utils.AppUtils;

@Service
public class CardColumnServiceImpl implements CardColumnService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private CardColumnRepository cardColumnRepository;
	
	@Autowired
	private AppUtils appUtils;
	
	@Override
	public CardColumn findById(Long userId, Long columnId) {
		return cardColumnRepository.findUserColumnById(userId, columnId)
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
		appUtils.assertStatusIsDisabled(found.getStatus());
		found.setTitle(cardColumn.getTitle());
		return cardColumnRepository.save(found);
	}

	@Override
	@Transactional
	public void updateColumnsOrder(Long userId, Long boardId, Map<Long, Integer> orderMap) {
		List<CardColumn> columns = cardColumnRepository.findByIdIn(boardId, orderMap.keySet());
		for (CardColumn column : columns) {
			Integer newOrder = orderMap.get(column.getId());
			column.setColumnOrder(newOrder);
		}
		cardColumnRepository.saveAll(columns);
	}

	@Override
	@Transactional
	public void disableColumn(Long userId, Long cardColumnId) {
		CardColumn column = findById(userId, cardColumnId);
		Status status = column.getStatus();
		appUtils.assertStatusIsDisabled(status);
		status.setEnabled(false);
		cardColumnRepository.save(column);
	}
	
	@Override
	@Transactional
	public CardColumn enableColumn(Long userId, Long cardColumnId) {
		CardColumn column = findById(userId, cardColumnId);
		column.getStatus().setEnabled(true);
		return cardColumnRepository.save(column);
	}

	@Override
	@Transactional
	public void deleteColumn(Long userId, Long cardColumnId) {
		CardColumn column = findById(userId, cardColumnId);
		Status status = column.getStatus();
		if (status.isEnabled()) 
			throw new DeleteEnabledEntityException("Kh??ng th??? x??a c???t ch??a v?? hi???u h??a");
		cardColumnRepository.delete(column);
	}
	
	private Board getCurrentBoard(Long userId, Long boardId) {
		return boardRepository.findByStatusEnabledIsTrueAndAppUserIdAndId(userId, boardId)
			.orElseThrow(() -> new BoardNotFoundException("B???ng " + boardId + " kh??ng c?? s???n"));
	}
	
	private CardColumnNotFoundException getColumnNotFoundException(Long colId) {
		return new CardColumnNotFoundException("C???t " + colId + " kh??ng c?? s???n");
	}

}
