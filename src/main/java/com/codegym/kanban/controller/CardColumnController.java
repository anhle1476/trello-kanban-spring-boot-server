package com.codegym.kanban.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codegym.kanban.dto.CardColumnInfoDTO;
import com.codegym.kanban.model.AppUser;
import com.codegym.kanban.model.CardColumn;
import com.codegym.kanban.service.CardColumnService;
import com.codegym.kanban.utils.AppUtils;

@RestController
@RequestMapping("api/v1/boards/{boardId}/columns")
public class CardColumnController {
	
	@Autowired
	private AppUtils appUtils;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CardColumnService cardColumnService;
	
	@PostMapping
	public ResponseEntity<?> addColumn(
			Principal principal,
			@PathVariable("boardId") Long boardId,
			@Valid @RequestBody CardColumn cardColumn,
			BindingResult result
			) {
		if (result.hasErrors())
			return appUtils.mapErrorToResponse(result);
		
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		
		CardColumn saved = cardColumnService.saveColumn(user.getId(), boardId, cardColumn);
		
		CardColumnInfoDTO dto = modelMapper.map(saved, CardColumnInfoDTO.class);
		return new ResponseEntity<CardColumnInfoDTO>(dto, HttpStatus.CREATED);
	}
}
