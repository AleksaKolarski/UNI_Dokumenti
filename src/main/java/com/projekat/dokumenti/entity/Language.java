package com.projekat.dokumenti.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "languages")
public class Language {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "name", unique = false, nullable = false, length = 30)
	private String name;
	
	@OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
	private List<EBook> ebooks = new ArrayList<>();
	
	
	public Language() {}
	

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
	
	public List<EBook> getEbooks() {
		return ebooks;
	}

	public void setEbooks(List<EBook> ebooks) {
		this.ebooks = ebooks;
	}

	@Override
	public String toString() {
		return "Language ["
				+ "id=" + id + ", "
				+ "name=" + name 
				+ "]";
	}
}
