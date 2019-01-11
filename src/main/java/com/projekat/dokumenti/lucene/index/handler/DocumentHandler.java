package com.projekat.dokumenti.lucene.index.handler;

import java.io.File;

import org.apache.lucene.document.Document;

public abstract class DocumentHandler {
	
	public abstract Document getDocument(File file);
	public abstract String getText(File file);
}
