package com.projekat.dokumenti.dto;

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
}
