package com.projekat.dokumenti.parser;

import java.io.File;

public class CustomDocumentParser {
	
	public static CustomParsedDocument parseDocument(File file) {
		
		String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		switch (extension) {
		case "txt":
			return new CustomTextDocParser().parseDocument(file);
		case "pdf":
			return new CustomPDFParser().parseDocument(file);
		case "doc":
			return new CustomWordParser().parseDocument(file);
		case "docx":
			return new CustomWord2007Parser().parseDocument(file);
		case "xml":
			System.out.println("XML HANDLER NOT IMPLEMENTED");
		}
		System.out.println("File extension not recognised. Returning null.");
		return null;
	}
	
public static CustomParsedDocument parseMetadata(File file) {
		
		String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		switch (extension) {
		case "txt":
			return new CustomTextDocParser().parseMetadata(file);
		case "pdf":
			return new CustomPDFParser().parseMetadata(file);
		case "doc":
			return new CustomWordParser().parseMetadata(file);
		case "docx":
			return new CustomWord2007Parser().parseMetadata(file);
		case "xml":
			System.out.println("XML HANDLER NOT IMPLEMENTED");
		}
		System.out.println("File extension not recognised. Returning null.");
		return null;
	}
}
