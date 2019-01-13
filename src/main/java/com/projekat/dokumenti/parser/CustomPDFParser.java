package com.projekat.dokumenti.parser;

import java.io.File;
import java.io.FileInputStream;
import java.time.ZoneId;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

public class CustomPDFParser implements CustomDocumentParserInterface {

	@Override
	public CustomParsedDocument parseDocument(File file) {
		CustomParsedDocument parsedDoc = new CustomParsedDocument();
		
		try {
			PDFParser parser = new PDFParser(new FileInputStream(file));
			parser.parse();
			
			PDDocument pdf = parser.getPDDocument();
			PDDocumentInformation info = pdf.getDocumentInformation();
			
			PDFTextStripper textStripper = new PDFTextStripper("utf-8");
			
			
			// Text
			parsedDoc.setText("" + textStripper.getText(parser.getPDDocument()));
			
			// Filename
			parsedDoc.setFilename(file.getName());
			
			// Title
			parsedDoc.setTitle("" + info.getTitle());
			
			// Author
			parsedDoc.setAuthor("" + info.getAuthor());
			
			// Keywords
			parsedDoc.setKeywords("" + info.getKeywords());
			
			// Publication year
			parsedDoc.setPublicationYear(info.getCreationDate().getTime().getYear());
			
			
			pdf.close();
			
			return parsedDoc;
		}
		catch (Exception e) {
			System.out.println("Greska pri parsiranju pdf dokumenta");
		}
		
		return null;
	}

	@Override
	public CustomParsedDocument parseMetadata(File file) {
		CustomParsedDocument parsedDoc = new CustomParsedDocument();
		
		try {
			PDFParser parser = new PDFParser(new FileInputStream(file));
			parser.parse();
			
			PDDocument pdf = parser.getPDDocument();
			PDDocumentInformation info = pdf.getDocumentInformation();
						
			// Filename
			parsedDoc.setFilename(file.getName());
			
			// Title
			parsedDoc.setTitle("" + info.getTitle());
			
			// Author
			parsedDoc.setAuthor("" + info.getAuthor());
			
			// Keywords
			parsedDoc.setKeywords("" + info.getKeywords());
			
			// Publication year
			parsedDoc.setPublicationYear(info.getCreationDate().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear());
			
			
			pdf.close();
			
			return parsedDoc;
		}
		catch (Exception e) {
			System.out.println("Greska pri parsiranju pdf dokumenta");
		}
		
		return null;
	}
	
}
