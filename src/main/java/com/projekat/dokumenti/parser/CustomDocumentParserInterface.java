package com.projekat.dokumenti.parser;

import java.io.File;

public interface CustomDocumentParserInterface {
	
	public CustomParsedDocument parseDocument(File file);
	
	public CustomParsedDocument parseMetadata(File file);
	
}
