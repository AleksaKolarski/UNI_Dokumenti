package com.projekat.dokumenti.lucene.index;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import com.projekat.dokumenti.entity.EBook;
import com.projekat.dokumenti.parser.CustomDocumentParser;

public class DocumentHandler {
	
	public static Document getDocument(File file, EBook ebook) {
		String parsedText = CustomDocumentParser.parseDocumentText(file);
		Document doc = new Document();
		if(ebook.getId() == null) {
			doc.add(new IntField("id", ebook.getId(), Store.YES));
		}
		if(parsedText != null) {
			doc.add(new TextField("text", parsedText, Store.NO));
		}
		if(ebook.getTitle() != null) {
			doc.add(new TextField("title", ebook.getTitle(), Store.YES));
		}
		if(ebook.getAuthor() != null) {
			doc.add(new TextField("author", ebook.getAuthor(), Store.YES));
		}
		String[] splittedKeywords = ebook.getKeywords().split("[;, ]+");
		for(String keyword: splittedKeywords) {
			doc.add(new TextField("keyword", keyword, Store.YES));
		}
		if(ebook.getPublicationYear() != null) {
			doc.add(new IntField("publicationYear", ebook.getPublicationYear(), Store.YES));
		}
		if(ebook.getPublicationYear() != null) {
			doc.add(new TextField("language", ebook.getLanguage().getName(), Store.YES));
		}
		if(ebook.getFilename() != null) {
			doc.add(new StringField("filename", ebook.getFilename(), Store.YES));
		}
		return doc;
	}
}
