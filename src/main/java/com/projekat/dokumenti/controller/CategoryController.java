package com.projekat.dokumenti.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.dto.CategoryDTO;
import com.projekat.dokumenti.entity.Category;
import com.projekat.dokumenti.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/all")
	public ResponseEntity<List<CategoryDTO>> getAllCategories(){
		return new ResponseEntity<>(CategoryDTO.parseList(categoryService.findAll()), HttpStatus.OK);
	}
	
	@GetMapping("/getById")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> getById(@RequestParam("categoryId") Integer categoryId) {
		Category category = categoryService.findById(categoryId);
		if(category == null) {
			logger.info("/category/getById | could not find category with id=" + categoryId);
			return new ResponseEntity<CategoryDTO>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<CategoryDTO>(new CategoryDTO(category), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> create(@RequestBody(required = true) CategoryDTO categoryDTO){
		
		String name = categoryDTO.getName();
		
		if(name == null || name.length() < 5 || name.length() > 30) {
			logger.info("/category/create | name not valid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Category category = new Category();
		category.setName(name);
		
		category = categoryService.save(category);
		
		if(category == null) {
			logger.info("/category/create | could not save category");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> edit(@RequestBody(required = true) CategoryDTO categoryDTO){
		
		Category category;
		Integer id = categoryDTO.getId();
		
		if(id == null) {
			logger.info("/category/edit | ID not present");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		category = categoryService.findById(id);
		
		if(category == null) {
			logger.info("/category/edit | could not find category with id=" + id);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		String name = categoryDTO.getName();
		
		if(name == null || name.length() < 5 || name.length() > 30) {
			logger.info("/category/edit | name not valid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		category.setName(name);
		
		category = categoryService.save(category);
		
		if(category == null) {
			logger.info("/category/edit | could not save category");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new CategoryDTO(category), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> delete(@RequestParam("categoryId") Integer categoryId){
		if(categoryId == null) {
			logger.info("/category/delete |  ID not present");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Category category = categoryService.findById(categoryId);
		if(category == null) {
			logger.info("/category/delete | could not find category with id=" + categoryId);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		categoryService.remove(category);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
