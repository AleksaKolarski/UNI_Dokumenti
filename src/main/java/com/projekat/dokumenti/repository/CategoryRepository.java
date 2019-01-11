package com.projekat.dokumenti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projekat.dokumenti.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	Category findByName(String name);
	
}
