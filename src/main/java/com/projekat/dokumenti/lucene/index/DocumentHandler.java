package com.projekat.dokumenti.lucene.index;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import com.projekat.dokumenti.parser.CustomDocumentParser;
import com.projekat.dokumenti.parser.CustomParsedDocument;

public class DocumentHandler {
	
	public static Document getDocument(File file) {
		CustomParsedDocument customParsedDoc = CustomDocumentParser.parseDocument(file);
		if(customParsedDoc != null) {
			Document doc = new Document();
			if(customParsedDoc.getText() != null) {
				doc.add(new TextField("text", customParsedDoc.getText(), Store.NO));
			}
			if(customParsedDoc.getTitle() != null) {
				doc.add(new TextField("title", customParsedDoc.getTitle(), Store.YES));
			}
			if(customParsedDoc.getAuthor() != null) {
				doc.add(new TextField("author", customParsedDoc.getAuthor(), Store.YES));
			}
			String[] splittedKeywords = customParsedDoc.getKeywords().split(";, ");
			for(String keyword: splittedKeywords) {
				doc.add(new TextField("keyword", keyword, Store.YES));
			}
			if(customParsedDoc.getPublicationYear() != null) {
				doc.add(new IntField("publicationYear", customParsedDoc.getPublicationYear(), Store.YES));
			}
			if(customParsedDoc.getFilename() != null) {
				doc.add(new StringField("filename", customParsedDoc.getFilename(), Store.YES));
			}
			return doc;
		}
		return null;
	}
}
