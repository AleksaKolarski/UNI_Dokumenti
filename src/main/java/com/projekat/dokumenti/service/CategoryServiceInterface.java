package com.projekat.dokumenti.service;

import java.util.List;

import com.projekat.dokumenti.entity.Category;

public interface CategoryServiceInterface {
	
	List<Category> findAll();
	
	Category findByName(String name);
	
	Category findById(Integer id);
	
	Category save(Category category);
	
	void remove(Category category);
	
}
