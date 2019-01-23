package com.projekat.dokumenti.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "ebooks")
public class EBook {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "title", unique = true, nullable = false, length = 80)
	private String title;
	
	@Column(name = "author", unique = false, nullable = true, length = 120)
	private String author;
	
	@Column(name = "keywords", unique = false, nullable = true, length = 120)
	private String keywords;
	
	@Column(name = "publication_year", unique = false, nullable = true)
	private Integer publicationYear;
	
	@Column(name = "filename", unique = true, nullable = false, length = 200)
	private String filename;
	
	@Column(name = "document_name", unique = false, nullable = false, length = 200)
	private String documentName;
	
	@Column(name = "mime", unique = false, nullable = true, length = 100)
	private String mime;
	
	@ManyToOne
	@JoinColumn(name = "language_id", referencedColumnName = "id", unique = false, nullable = false)
	private Language language;
	
	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id", unique = false, nullable = false)
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", unique = false, nullable = false)
	private User user;
	
	
	public EBook() {}

	
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

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
				+ "documentName" + documentName + ", "
				+ "mime=" + mime + ", "
				+ "language=" + language.getName() + ", "
				+ "category=" + category.getName() + ", "
				+ "user=" + user.getUsername()
				+ "]";
	}
}
