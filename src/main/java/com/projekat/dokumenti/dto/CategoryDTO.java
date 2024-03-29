package com.projekat.dokumenti.dto;

import java.util.ArrayList;
import java.util.List;

import com.projekat.dokumenti.entity.Category;

public class CategoryDTO {
	
	private Integer id;
	private String name;
	
	
	public CategoryDTO() {}
	
	public CategoryDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public CategoryDTO(Category category) {
		this(category.getId(), category.getName());
	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	@Override
	public String toString() {
		return "Category ["
				+ "id=" + id + ", "
				+ "name=" + name
				+ "]";
	}
	
	public static List<CategoryDTO> parseList(List<Category> list){
		List<CategoryDTO> listDTO = new ArrayList<CategoryDTO>();
		for(Category language: list) {
			listDTO.add(new CategoryDTO(language));
		}
		return listDTO;
	}
}
