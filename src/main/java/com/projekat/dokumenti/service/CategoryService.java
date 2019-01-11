package com.projekat.dokumenti.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekat.dokumenti.entity.Category;
import com.projekat.dokumenti.repository.CategoryRepository;

@Service
public class CategoryService implements CategoryServiceInterface {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Category findByName(String name) {
		return categoryRepository.findByName(name);
	}

	@Override
	public Category findById(Integer id) {
		Optional<Category> optional = categoryRepository.findById(id);
		if(optional.isPresent() == false)
			return null;
		return optional.get();
	}

	@Override
	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public void remove(Category category) {
		categoryRepository.delete(category);
	}

}
