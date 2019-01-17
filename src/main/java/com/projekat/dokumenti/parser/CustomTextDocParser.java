package com.projekat.dokumenti.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CustomTextDocParser {

	public String parseDocumentText(File file) {
		
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			
			// Title
			reader.readLine();
			
			// Keywords
			reader.readLine();
			
			// Text
			String fullText = "";
			while(true) {
				String line = reader.readLine();
				if(line == null) {
					break;
				}
				fullText += " " + line;
			}
			
			reader.close();
			return fullText;
		}
		catch (Exception e) {
			System.out.println("Greska pri parsiranju pdf dokumenta");
		}
		
		return null;
	}

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
			
			reader.close();
			
			return parsedDoc;
		}
		catch (Exception e) {
			System.out.println("Greska pri parsiranju pdf dokumenta");
		}
		
		return null;
	}

}
