package com.projekat.dokumenti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.dto.LanguageDTO;
import com.projekat.dokumenti.service.LanguageService;

@RestController
@RequestMapping("/language")
public class LanguageController {
		
	@Autowired
	private LanguageService languageService;
	
	@GetMapping("/all")
	public ResponseEntity<List<LanguageDTO>> getAllLanguages(){
		return new ResponseEntity<>(LanguageDTO.parseList(languageService.findAll()), HttpStatus.OK);
	}
}
