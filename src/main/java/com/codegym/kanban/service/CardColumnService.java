package com.codegym.kanban.service;

import java.util.Map;

import com.codegym.kanban.model.CardColumn;

public interface CardColumnService {
	CardColumn saveColumn(Long userId, Long boardId, CardColumn cardColumn);
	
	CardColumn updateColumn(Long userId, Long boardId, CardColumn cardColumn);
	
	void updateColumnsOrder(Long userId, Long boardId, Map<Long, Long> orderMap);
	
	void disableColumn(Long userId, Long boardId, Long cardColumnId);
	
	void deleteColumn(Long userId, Long boardId, Long cardColumnId);
}
