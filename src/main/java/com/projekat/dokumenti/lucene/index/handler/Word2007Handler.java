package com.projekat.dokumenti.lucene.index.handler;

import java.io.File;
import java.io.FileInputStream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Word2007Handler extends DocumentHandler {

	@Override
	public Document getDocument(File file) {
		Document doc = new Document();
		try {
			XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
			XWPFWordExtractor we = new XWPFWordExtractor(wordDoc);
			
			String text = we.getText();
			doc.add(new TextField("text", text, Store.NO));
			
			POIXMLProperties props = wordDoc.getProperties();
			
			String title = props.getCoreProperties().getTitle();
			doc.add(new TextField("title", title, Store.YES));
			
			String keywords = props.getCoreProperties().getUnderlyingProperties().getKeywordsProperty().getValue();
			String[] splittedKeywords = keywords.split(" ");
			for(String keyword: splittedKeywords) {
				doc.add(new TextField("keyword", keyword, Store.YES));
			}
			doc.add(new StringField("filename", file.getCanonicalPath(), Store.YES));
			return doc;
		}
		catch (Exception e) {
			System.out.println("Problem pri parsiranju docx fajla");
		}
		return null;
	}

	@Override
	public String getText(File file) {
		try {
			XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
			XWPFWordExtractor we = new XWPFWordExtractor(wordDoc);
			String text = we.getText();
			return text;
		}
		catch (Exception e) {
			System.out.println("Problem pri parsiranju docx fajla");
		}
		return null;
	}

}
