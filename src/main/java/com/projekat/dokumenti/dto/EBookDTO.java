package com.projekat.dokumenti.dto;

import com.projekat.dokumenti.entity.EBook;

public class EBookDTO {
	
	private Integer id;
	private String title;
	private String author;
	private String keywords;
	private Integer publicationYear;
	private String filename;
	private String mime;
	
	
	public EBookDTO() {}
	
	public EBookDTO(Integer id, String title, String author, String keywords, Integer publicationYear, String filename, String mime) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.keywords = keywords;
		this.publicationYear = publicationYear;
		this.filename = filename;
		this.mime = mime;
	}
	
	public EBookDTO(EBook ebook) {
		this(ebook.getId(), ebook.getTitle(), ebook.getAuthor(), ebook.getKeywords(), ebook.getPublicationYear(), ebook.getFilename(), ebook.getMime());
	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}
	
	
	@Override
	public String toString() {
		return "EBook ["
				+ "id=" + id + ", "
				+ "title=" + title + ", "
				+ "author=" + author + ", "
				+ "keywords=" + keywords + ", "
				+ "publicationYear=" + publicationYear + ", "
				+ "filename=" + filename + ", "
				+ "mime=" + mime + ", "
				+ "]";
	}
}
