package com.projekat.dokumenti.lucene.index.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;

public class TextDocHandler extends DocumentHandler {

	@Override
	public Document getDocument(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF8"));
			
			String firstLine = reader.readLine(); // Naslov rada
			
			Document doc = new Document();
			
			doc.add(new TextField("title", firstLine, Store.YES));
			doc.add(new StringField("filename", file.getCanonicalPath(), Store.YES));
			
			/*
			 * add other custom metadata
			 */
			
			String secondLine = reader.readLine();
			String[] keywords = secondLine.split(";");
			for(String kw: keywords) {
				doc.add(new TextField("keyword", kw, Store.YES));
			}
			
			String fullText = "";
			while(true) {
				secondLine = reader.readLine();
				if(secondLine == null) {
					break;
				}
				fullText += " " + secondLine;
			}
			doc.add(new TextField("text", fullText, Store.NO));
			reader.close();
			
			return doc;
		}
		catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Datoteka ne postoji");
		}
		catch (IOException e) {
			throw new IllegalArgumentException("Datoteka nije u redu");
		}
	}

	
	@Override
	public String getText(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis, "UTF8"));
			String secondLine;
			String fullText = "";
			while (true) {
				secondLine = reader.readLine();
				if (secondLine == null) {
					break;
				}
				fullText += " " + secondLine;
			}
			reader.close();
			return fullText;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Datoteka ne postoji");
		} catch (IOException e) {
			throw new IllegalArgumentException("Greska: Datoteka nije u redu");
		}
	}
}
