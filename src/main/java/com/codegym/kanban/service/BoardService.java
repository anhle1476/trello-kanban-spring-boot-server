package com.codegym.kanban.service;

import java.util.List;

import com.codegym.kanban.dto.BoardInfoDTO;
import com.codegym.kanban.model.Board;

public interface BoardService {
	List<BoardInfoDTO> getAvailableBoards(Long userId);

	List<BoardInfoDTO> getDisabledBoards(Long userId);

	Board getBoardDetails(Long userId, Long id);
	
	Board saveBoard(Long userId, Board board);
	
	Board updateBoard(Long userId, Board board);
	
	void disableBoard(Long userId, Long id);
	
	Board enableBoard(Long userId, Long id);
	
	void deleteBoard(Long userId, Long id);

}
