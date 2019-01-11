package com.projekat.dokumenti.service;

import java.util.List;

import com.projekat.dokumenti.entity.Language;

public interface LanguageServiceInterface {
	
	List<Language> findAll();
	
	Language findByName(String name);
	
	Language findById(Integer id);
	
	Language save(Language language);
	
	void remove(Language language);
	
}
