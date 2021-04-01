package com.codegym.kanban.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
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

import com.codegym.kanban.dto.CardColumnInfoDTO;
import com.codegym.kanban.exception.BadRequestFieldException;
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
		
		return new ResponseEntity<CardColumn>(saved, HttpStatus.CREATED);
	}
	
	@PutMapping("/{columnId}")
	public ResponseEntity<?> updateColumn(
			Principal principal,
			@PathVariable("boardId") Long boardId,
			@PathVariable("columnId") Long columnId,
			@Valid @RequestBody CardColumn cardColumn,
			BindingResult result
			) {
		if (result.hasErrors())
			return appUtils.mapErrorToResponse(result);
		
		if (columnId != cardColumn.getId())
			throw new BadRequestFieldException("Id cột không hợp lệ");
		
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		
		CardColumn updated = cardColumnService.updateColumn(user.getId(), cardColumn);
		
		CardColumnInfoDTO dto = modelMapper.map(updated, CardColumnInfoDTO.class);
		return new ResponseEntity<CardColumnInfoDTO>(dto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{columnId}")
	public void deleteColumn(
			Principal principal,
			@PathVariable("boardId") Long boardId,
			@PathVariable("columnId") Long columnId,
			@RequestParam(name = "type", defaultValue = "archived") String type
			) {
		
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		
		if (type.equals("permanent")) 
			cardColumnService.deleteColumn(user.getId(), columnId);
		else 
			cardColumnService.disableColumn(user.getId(), columnId);
	}
	
	@PutMapping("/{columnId}/enable")
	public ResponseEntity<CardColumn> enableColumn(
			Principal principal,
			@PathVariable("boardId") Long boardId,
			@PathVariable("columnId") Long columnId
			) {
		AppUser user = appUtils.extractUserInfoFromToken(principal);
		CardColumn enabledColumn = cardColumnService.enableColumn(user.getId(), columnId);
		return new ResponseEntity<CardColumn>(enabledColumn, HttpStatus.OK);
	}
}
