package com.projekat.dokumenti.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.ZoneId;

import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class CustomWordParser {

	public String parseDocumentText(File file) {
				
		try {
			InputStream is = new FileInputStream(file);
			WordExtractor we = new WordExtractor(is);
			
			// Text
			return we.getText();
		}
		catch (Exception e) {
			System.out.println("Greska pri parsiranju doc fajla");
		}
		
		return null;
	}

	public CustomParsedDocument parseMetadata(File file) {
		CustomParsedDocument parsedDoc = new CustomParsedDocument();
		
		try {
			InputStream is = new FileInputStream(file);
			WordExtractor we = new WordExtractor(is);
			SummaryInformation si = we.getSummaryInformation();		
			
			// Filename
			parsedDoc.setFilename(file.getName());	
			
			// Title
			parsedDoc.setTitle("" + si.getTitle());
						
			// Author
			parsedDoc.setAuthor("" + si.getAuthor());
						
			// Keywords
			parsedDoc.setKeywords("" + si.getKeywords());
						
			// Publication year
			parsedDoc.setPublicationYear(si.getCreateDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear());
			
			// MIME
			parsedDoc.setMime("application/msword");
			
			return parsedDoc;
		}
		catch (Exception e) {
			System.out.println("Greska pri parsiranju doc fajla");
		}
		return null;
	}

}
