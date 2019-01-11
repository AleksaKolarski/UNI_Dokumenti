package com.projekat.dokumenti.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.dto.LanguageDTO;
import com.projekat.dokumenti.service.LanguageService;

@RestController
@RequestMapping("/language")
public class LanguageController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	@Autowired
	private LanguageService languageService;
	
	@GetMapping("/all")
	public List<LanguageDTO> getAllLanguages(){
		return LanguageDTO.parseList(languageService.findAll());
	}
}
