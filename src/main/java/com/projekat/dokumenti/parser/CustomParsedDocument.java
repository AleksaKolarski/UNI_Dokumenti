package com.projekat.dokumenti.parser;

public class CustomParsedDocument {
	
	private String documentName;
	private String title;
	private String author;
	private String keywords;
	private Integer publicationYear;
	private String text;
	
	private String filename;
	private String mime;
	
	
	public CustomParsedDocument() {
		
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

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
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
	
	public String getDocumentName() {
		return documentName;
	}
	
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}


	@Override
	public String toString() {
		return "CustomParsedDocument ["
				+ "title=" + title + ", "
				+ "author=" + author + ", "
				+ "keywords=" + keywords + ", "
				+ "publicationYear=" + publicationYear + ", "
				+ "text=" + text + ", "
				+ "filename=" + filename + ", "
				+ "documentName=" + documentName + ", "
				+ "mime=" + mime
				+ "]";
	}
}
