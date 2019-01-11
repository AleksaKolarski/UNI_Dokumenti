package com.projekat.dokumenti.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.dto.EBookDTO;
import com.projekat.dokumenti.service.EBookService;

@RestController
@RequestMapping("/book")
public class EBookController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	@Autowired
	private EBookService ebookService;
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<EBookDTO> getAllEBooks(){
		return EBookDTO.parseList(ebookService.findAll());
	}
}
