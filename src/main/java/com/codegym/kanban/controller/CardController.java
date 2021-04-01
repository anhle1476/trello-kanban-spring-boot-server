package com.codegym.kanban.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codegym.kanban.exception.BadRequestFieldException;
import com.codegym.kanban.model.AppUser;
import com.codegym.kanban.model.Card;
import com.codegym.kanban.service.CardService;
import com.codegym.kanban.utils.AppUtils;

@RestController
@RequestMapping("api/v1/boards/{boardId}/columns/{columnId}/cards")
public class CardController {
	
	@Autowired
	private AppUtils appUtils;
	
	@Autowired
	private CardService cardService;
	
	
	@PostMapping
	public ResponseEntity<?> addCard(
			Principal principal,
			@PathVariable("boardId") Long boardId,
			@PathVariable("columnId") Long columnId,
			@Valid @RequestBody Card card,
			BindingResult result
			) {
		if (result.hasErrors())
			return appUtils.mapErrorToResponse(result);
		
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		
		Card saved = cardService.saveCard(user.getId(), columnId, card);
		
		return new ResponseEntity<Card>(saved, HttpStatus.CREATED);
	}
	
	@PutMapping("/{cardId}")
	public ResponseEntity<?> updateColumn(
			Principal principal,
			@PathVariable("boardId") Long boardId,
			@PathVariable("columnId") Long columnId,
			@PathVariable("cardId") Long cardId,
			@Valid @RequestBody Card card,
			BindingResult result
			) {
		if (result.hasErrors())
			return appUtils.mapErrorToResponse(result);
		
		if (cardId != card.getId())
			throw new BadRequestFieldException("Id thẻ không hợp lệ");
		
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		
		Card updated = cardService.updateCard(user.getId(), card);
		
		return new ResponseEntity<Card>(updated, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{cardId}")
	public void deleteCard(
			Principal principal,
			@PathVariable("boardId") Long boardId,
			@PathVariable("columnId") Long columnId,
			@PathVariable("cardId") Long cardId,
			@RequestParam(name = "type", defaultValue = "archived") String type
			) {
		
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		
		if (type.equals("permanent")) 
			cardService.deleteCard(user.getId(), cardId);
		else 
			cardService.disableCard(user.getId(), cardId);
	}
	
	@PutMapping("/{cardId}/enable")
	public ResponseEntity<Card> enableColumn(
			Principal principal,
			@PathVariable("boardId") Long boardId,
			@PathVariable("columnId") Long columnId,
			@PathVariable("cardId") Long cardId
			) {
		AppUser user = appUtils.extractUserInfoFromToken(principal);		
		Card enabled = cardService.enableCard(user.getId(), cardId);
		
		return new ResponseEntity<Card>(enabled, HttpStatus.CREATED);
	}
}
