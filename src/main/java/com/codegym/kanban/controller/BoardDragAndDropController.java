package com.codegym.kanban.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codegym.kanban.dto.CardOrderDifferDTO;
import com.codegym.kanban.model.AppUser;
import com.codegym.kanban.service.CardColumnService;
import com.codegym.kanban.service.CardService;
import com.codegym.kanban.utils.AppUtils;

@RestController
@RequestMapping("api/v1/dnd/{boardId}")
public class BoardDragAndDropController {
	
	@Autowired
	private CardColumnService cardColumnService;
	
	@Autowired
	private CardService cardService;
	
	
	@Autowired
	private AppUtils appUtils;
	
	@PutMapping("/columns")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void reorderColumns(
			@PathVariable("boardId") Long boardId,
			@RequestBody Map<Long, Integer> orderMap,
			Principal principal
			) {
		AppUser user = appUtils.extractUserInfoFromToken(principal);	
		cardColumnService.updateColumnsOrder(user.getId(), boardId, orderMap);
	}
	
	@PutMapping("/cards")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void reorderCards(
			@PathVariable("boardId") Long boardId,
			@RequestBody Map<Long, CardOrderDifferDTO> orderMap,
			Principal principal
			) {
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		
		System.out.println(user);
		System.out.println(boardId);
		System.out.println(orderMap);
		
		cardService.updateCardsOrder(user.getId(), boardId, orderMap);
	}
}
