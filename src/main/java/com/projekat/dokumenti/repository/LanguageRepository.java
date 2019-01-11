package com.projekat.dokumenti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projekat.dokumenti.entity.Language;

public interface LanguageRepository extends JpaRepository<Language, Integer>{
	
	Language findByName(String name);
	
}
