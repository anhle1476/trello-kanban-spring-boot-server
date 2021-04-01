package com.codegym.kanban.service;

import java.util.Map;

import com.codegym.kanban.model.CardColumn;

public interface CardColumnService {

	CardColumn findById(Long userId, Long cardColumnId);
	
	CardColumn saveColumn(Long userId, Long boardId, CardColumn cardColumn);
	
	CardColumn updateColumn(Long userId, CardColumn cardColumn);
	
	void updateColumnsOrder(Long userId, Long boardId, Map<Long, Integer> orderMap);
	
	void disableColumn(Long userId, Long cardColumnId);
	
	CardColumn enableColumn(Long userId, Long cardColumnId);
	
	void deleteColumn(Long userId, Long cardColumnId);

}
