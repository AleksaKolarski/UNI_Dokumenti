package com.projekat.dokumenti.parser;

import java.io.File;
import java.io.FileInputStream;
import java.time.ZoneId;

import org.apache.poi.POIXMLProperties;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class CustomWord2007Parser implements CustomDocumentParserInterface{

	@Override
	public CustomParsedDocument parseDocument(File file) {
		CustomParsedDocument parsedDoc = new CustomParsedDocument();
		
		try {
			XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
			XWPFWordExtractor we = new XWPFWordExtractor(wordDoc);
			POIXMLProperties props = wordDoc.getProperties();
			
			// Text
			parsedDoc.setText("" + we.getText());
			
			// Title
			parsedDoc.setTitle("" + props.getCoreProperties().getTitle());
			
			// Author
			parsedDoc.setAuthor("" + props.getCoreProperties().getCreator());
			
			// Filename
			parsedDoc.setFilename(file.getName());
			
			// Keywords
			parsedDoc.setKeywords(props.getCoreProperties().getKeywords());
			
			// Publication year
			parsedDoc.setPublicationYear(props.getCoreProperties().getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear());
			
			return parsedDoc;
		}
		catch (Exception e) {
			System.out.println("Greska pri parsiranju docx fajla");
		}
		
		return null;
	}

	@Override
	public CustomParsedDocument parseMetadata(File file) {
		CustomParsedDocument parsedDoc = new CustomParsedDocument();
		
		try {
			XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
			POIXMLProperties props = wordDoc.getProperties();
			
			// Title
			parsedDoc.setTitle("" + props.getCoreProperties().getTitle());
			
			// Author
			parsedDoc.setAuthor("" + props.getCoreProperties().getCreator());
			
			// Filename
			parsedDoc.setFilename(file.getName());
			
			// Keywords
			parsedDoc.setKeywords(props.getCoreProperties().getKeywords());
			
			// Publication year
			parsedDoc.setPublicationYear(props.getCoreProperties().getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear());
			
			return parsedDoc;
		}
		catch (Exception e) {
			System.out.println("Greska pri parsiranju docx fajla");
		}
		
		return null;
	}

}