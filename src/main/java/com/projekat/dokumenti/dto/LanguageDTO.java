package com.projekat.dokumenti.dto;

import java.util.ArrayList;
import java.util.List;

import com.projekat.dokumenti.entity.Language;

public class LanguageDTO {
	
	private Integer id;
	private String name;
	
	
	public LanguageDTO() {}
	
	public LanguageDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public LanguageDTO(Language language) {
		this(language.getId(), language.getName());
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
		return "LanguageDTO ["
				+ "id=" + id + ", "
				+ "name=" + name 
				+ "]";
	}
	
	public static List<LanguageDTO> parseList(List<Language> list){
		List<LanguageDTO> listDTO = new ArrayList<LanguageDTO>();
		for(Language language: list) {
			listDTO.add(new LanguageDTO(language));
		}
		return listDTO;
	}
}
