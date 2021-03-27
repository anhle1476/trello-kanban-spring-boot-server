package com.codegym.kanban.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		
		Card saved = cardService.saveCard(user.getId(), boardId, columnId, card);
		
		return new ResponseEntity<Card>(saved, HttpStatus.CREATED);
	}
}
