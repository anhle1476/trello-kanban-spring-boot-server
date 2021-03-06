package com.codegym.kanban.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegym.kanban.dto.BoardInfoDTO;
import com.codegym.kanban.exception.BoardNotFoundException;
import com.codegym.kanban.exception.DeleteEnabledEntityException;
import com.codegym.kanban.exception.UserNotFoundException;
import com.codegym.kanban.model.AppUser;
import com.codegym.kanban.model.Board;
import com.codegym.kanban.repository.AppUserRepository;
import com.codegym.kanban.repository.BoardRepository;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private AppUserRepository appUserRepository;

	@Override
	public List<BoardInfoDTO> getAvailableBoards(Long userId) {
		return boardRepository.findByStatusEnabledIsTrueAndAppUserIdOrderByStatusUpdatedAtDesc(userId);
	}

	@Override
	public List<BoardInfoDTO> getDisabledBoards(Long userId) {
		return boardRepository.findByStatusEnabledIsFalseAndAppUserIdOrderByStatusUpdatedAtDesc(userId);
	}
	
	@Override
	public Board getBoardDetails(Long userId, Long id) {
		 Board board = boardRepository.findByStatusEnabledIsTrueAndAppUserIdAndId(userId, id)
				.orElseThrow(() -> getBoardException(id));
		 board.setLastedView(LocalDateTime.now());
		 return boardRepository.save(board);
	}

	@Transactional
	@Override
	public Board saveBoard(Long userId, Board board) {
		AppUser user = appUserRepository.findById(userId).orElseThrow(
				() -> new UserNotFoundException("User ID " + userId + " không tồn tại"));
		board.setAppUser(user);
		
		return boardRepository.save(board);
	}
	
	@Transactional
	@Override
	public Board updateBoard(Long userId, Board board) {

		Long boardId = board.getId();
		Board found = boardRepository.findById(boardId).orElseThrow(() -> getBoardException(boardId));
		if (found.getAppUser().getId() != userId)
			throw new BoardNotFoundException("Bảng không thuộc về tài khoản này");
		
		found.setTitle(board.getTitle());
		found.setColor(board.getColor());
		
		return boardRepository.save(found);
	}

	@Transactional
	@Override
	public void disableBoard(Long userId, Long id) {
		Integer affected = boardRepository.disableBoard(userId, id);
		if (affected == 0)
			throw getBoardException(id);
	}
	
	@Override
	public Board enableBoard(Long userId, Long id) {
		Board found = boardRepository.findById(id).orElseThrow(() -> getBoardException(id));
		if (found.getAppUser().getId() != userId)
			throw new BoardNotFoundException("Bảng không thuộc về tài khoản này");
		
		found.getStatus().setEnabled(true);
		return boardRepository.save(found);
	}


	@Transactional
	@Override
	public void deleteBoard(Long userId, Long id) {
		Board found = boardRepository.findById(id).orElseThrow(() -> getBoardException(id));
		if (found.getAppUser().getId() != userId)
			throw new BoardNotFoundException("Bảng không thuộc về tài khoản này");
		if (found.getStatus().isEnabled())
			throw new DeleteEnabledEntityException("Bảng đang hoạt động, không thể xóa");
		boardRepository.delete(found);
	}
	

	private BoardNotFoundException getBoardException(Long id) {
		return new BoardNotFoundException("Bảng ID " + id + " không tồn tại hoặc không thuộc về tài khoản này");
	}

	

	

}
