package com.projekat.dokumenti.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.dto.CategoryDTO;
import com.projekat.dokumenti.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/all")
	public List<CategoryDTO> getAllCategories(){
		return CategoryDTO.parseList(categoryService.findAll());
	}
}
