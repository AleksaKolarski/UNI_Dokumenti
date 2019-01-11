package com.projekat.dokumenti.service;

import java.util.List;

import com.projekat.dokumenti.entity.EBook;

public interface EBookServiceInterface {
	
	List<EBook> findAll();
	
	EBook findByTitle(String title);
	
	EBook findById(Integer id);
	
	EBook findByFilename(String filename);
	
	List<EBook> findByUserId(Integer userId);
	
	EBook save(EBook ebook);
	
	void remove(EBook ebook);
	
}
