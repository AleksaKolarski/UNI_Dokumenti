package com.projekat.dokumenti.dto;

import java.util.ArrayList;
import java.util.List;

import com.projekat.dokumenti.entity.EBook;

public class EBookDTO {
	
	private Integer id;
	private String title;
	private String author;
	private String keywords;
	private Integer publicationYear;
	private String filename;
	private String documentName;
	private String mime;
	private String languageName;
	private String categoryName;
	private String uploaderUsername;
	
	
	public EBookDTO() {}
	
	public EBookDTO(Integer id, String title, String author, String keywords, Integer publicationYear, String filename, String documentName, String mime) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.keywords = keywords;
		this.publicationYear = publicationYear;
		this.filename = filename;
		this.documentName = documentName;
		this.mime = mime;
	}
	
	public EBookDTO(EBook ebook) {
		this(ebook.getId(), ebook.getTitle(), ebook.getAuthor(), ebook.getKeywords(), ebook.getPublicationYear(), ebook.getFilename(), ebook.getDocumentName(), ebook.getMime());
		this.languageName = ebook.getLanguage().getName();
		this.categoryName = ebook.getCategory().getName();
		this.uploaderUsername = ebook.getUser().getUsername();
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

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}
	
	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getUploaderUsername() {
		return uploaderUsername;
	}

	public void setUploaderUsername(String uploaderUsername) {
		this.uploaderUsername = uploaderUsername;
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
				+ "documentName=" + documentName + ", "
				+ "mime=" + mime + ", "
				+ "]";
	}
	
	public static List<EBookDTO> parseList(List<EBook> list){
		List<EBookDTO> listDTO = new ArrayList<EBookDTO>();
		for(EBook ebook: list) {
			listDTO.add(new EBookDTO(ebook));
		}
		return listDTO;
	}
}
