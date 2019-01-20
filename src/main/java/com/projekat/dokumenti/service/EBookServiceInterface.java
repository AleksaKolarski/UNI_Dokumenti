package com.projekat.dokumenti.service;

import java.util.List;

import com.projekat.dokumenti.entity.EBook;

public interface EBookServiceInterface {
	
	List<EBook> findAll();
	
	List<EBook> findAllOrderByTitleDesc();
	
	List<EBook> findAllOrderByTitleAsc();
	
	EBook findByTitle(String title);
	
	EBook findById(Integer id);
	
	EBook findByFilename(String filename);
		
	List<EBook> findByUserId(Integer userId);
	
	List<EBook> findByCategoryName(String categoryName);
	
	List<EBook> findByCategoryNameOrderByDesc(String categoryName);
	
	List<EBook> findByCategoryNameOrderByAsc(String categoryName);
	
	EBook save(EBook ebook);
	
	void remove(EBook ebook);
	
}
