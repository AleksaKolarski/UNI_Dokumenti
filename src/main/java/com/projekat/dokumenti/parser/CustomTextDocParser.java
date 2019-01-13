package com.projekat.dokumenti.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CustomTextDocParser implements CustomDocumentParserInterface {

	@Override
	public CustomParsedDocument parseDocument(File file) {
		CustomParsedDocument parsedDoc = new CustomParsedDocument();
		
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			
			// Title
			parsedDoc.setTitle("" + reader.readLine());
			
			// Keywords
			parsedDoc.setKeywords("" + reader.readLine());
			
			// Text
			String fullText = "";
			while(true) {
				String line = reader.readLine();
				if(line == null) {
					break;
				}
				fullText += " " + line;
			}
			parsedDoc.setText(fullText);
					
			// Filename
			parsedDoc.setFilename(file.getName());
			
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
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF8"));
			
			// Title
			parsedDoc.setTitle("" + reader.readLine());
			
			// Keywords
			parsedDoc.setKeywords("" + reader.readLine());
					
			// Filename
			parsedDoc.setFilename(file.getName());
			
			return parsedDoc;
		}
		catch (Exception e) {
			System.out.println("Greska pri parsiranju pdf dokumenta");
		}
		
		return null;
	}

}
