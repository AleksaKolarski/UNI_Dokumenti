package com.projekat.dokumenti.lucene.index.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class WordHandler extends DocumentHandler {

	@Override
	public Document getDocument(File file) {
		InputStream is;
		Document doc = new Document();
		
		try {
			is = new FileInputStream(file);
			
			WordExtractor we = new WordExtractor(is);
			String text = we.getText();
			doc.add(new TextField("text", text, Store.YES));
			
			SummaryInformation si = we.getSummaryInformation();
			String title = si.getTitle();
			doc.add(new TextField("title", title, Store.YES));
			
			String keywords = si.getKeywords();
			String[] splittedKeywords = keywords.split(" ");
			for (String keyword: splittedKeywords) {
				doc.add(new TextField("keyword", keyword, Store.YES));
			}
			doc.add(new StringField("filename", file.getCanonicalPath(), Store.YES));
			
			return doc;
		}
		catch (Exception e) {
			System.out.println("Problem pri parsiranju doc fajla");
		}
		return null;
	}

	@Override
	public String getText(File file) {
		try {
			WordExtractor we = new WordExtractor(new FileInputStream(file));
			String text = we.getText();
			return text;
		} catch (Exception e) {
			System.out.println("Problem pri parsiranju doc fajla");
		}
		return null;
	}
}
