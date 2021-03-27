package com.codegym.kanban.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codegym.kanban.dto.BoardInfoDTO;
import com.codegym.kanban.exception.BadRequestFieldException;
import com.codegym.kanban.model.AppUser;
import com.codegym.kanban.model.Board;
import com.codegym.kanban.service.BoardService;
import com.codegym.kanban.utils.AppUtils;

@RestController
@RequestMapping("api/v1/boards")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private AppUtils appUtils;
	
	@GetMapping
	public ResponseEntity<List<BoardInfoDTO>> getAllBoardsWithStatus(
			@RequestParam(name = "type", defaultValue = "available") String type, 
			Principal principal) {
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		
		List<BoardInfoDTO> boards;
		if (type.equals("archived")) 
			boards = boardService.getDisabledBoards(user.getId());
		else 
			boards = boardService.getAvailableBoards(user.getId());

		return new ResponseEntity<List<BoardInfoDTO>>(boards, HttpStatus.OK); 
	}
	
	@GetMapping("/{boardId}")
	public ResponseEntity<Board> getBoardById(
			Principal principal, 
			@PathVariable("boardId") Long boardId) {
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		Board board = boardService.getBoardDetails(user.getId(), boardId);
		return new ResponseEntity<Board>(board, HttpStatus.OK); 
	}
	
	@PostMapping
	public ResponseEntity<?> createNewBoard(
			@Valid @RequestBody Board board, 
			BindingResult result,
			Principal principal) {
		if (result.hasErrors()) 
			return appUtils.mapErrorToResponse(result);
		

		if (board.getId() != null)
			throw new BadRequestFieldException("Không được tạo sẵn Id cho bảng");
		
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		Board savedBoard = boardService.saveBoard(user.getId(), board);
		
		return new ResponseEntity<Board>(savedBoard, HttpStatus.CREATED); 
	}
	
	@PutMapping("/{boardId}")
	public ResponseEntity<?> updateBoard(
			@Valid @RequestBody Board board, 
			BindingResult result,
			@PathVariable("boardId") Long boardId,
			Principal principal) {
		if (result.hasErrors()) 
			return appUtils.mapErrorToResponse(result);
		
		if (boardId != board.getId())
			throw new BadRequestFieldException("Id bảng không hợp lệ");
		
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		Board savedBoard = boardService.updateBoard(user.getId(), board);
		
		return new ResponseEntity<Board>(savedBoard, HttpStatus.CREATED); 
	}
	
	@DeleteMapping("/{boardId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void updateBoard(
			@PathVariable("boardId") Long boardId,
			@RequestParam(value = "type", defaultValue = "archived") String type,
			Principal principal) {
		
		AppUser user = appUtils.extractUserInfoFromToken(principal);

		if (type.equals("permanent")) 
			boardService.deleteBoard(user.getId(), boardId);
		else 
			boardService.disableBoard(user.getId(), boardId);
	}
	
}
