package com.projekat.dokumenti.lucene.index;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import com.projekat.dokumenti.lucene.analysers.SerbianAnalyzer;
import com.projekat.dokumenti.lucene.index.handler.DocumentHandler;
import com.projekat.dokumenti.lucene.index.handler.PDFHandler;
import com.projekat.dokumenti.lucene.index.handler.TextDocHandler;
import com.projekat.dokumenti.lucene.index.handler.Word2007Handler;
import com.projekat.dokumenti.lucene.index.handler.WordHandler;

public class Indexer {
	
	private IndexWriter indexWriter;
	private Directory indexDir;
	
	private static Indexer indexer = new Indexer(true);
	
	public static Indexer getInstance() {
		return indexer;
	}
	
	
	private Indexer(String path, boolean restart) {
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, new SerbianAnalyzer());
		if(restart) {
			iwc.setOpenMode(OpenMode.CREATE);
		}
		else {
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		
		try {
			this.indexDir = new SimpleFSDirectory(new File(path));
			this.indexWriter = new IndexWriter(this.indexDir, iwc);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Path not correct");
		}
		
	}
	
	private Indexer(boolean restart) {
		this(ResourceBundle.getBundle("luceneindex").getString("indexDir"), restart);
	}
	
	private Indexer() {
		this(false);
	}
	
	public boolean add(Document doc) {
		try {
			synchronized (this) {
				this.indexWriter.addDocument(doc);
				this.indexWriter.commit();
			}
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}
	
	public boolean delete(String filename){
		Term delTerm = new Term("filename", filename);
		try {
			synchronized (this) {
				this.indexWriter.deleteDocuments(delTerm);
				this.indexWriter.commit();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public void index(File file) {
		DocumentHandler handler = null;
		String fileName = null;
		
		try {
			File[] files;
			if(file.isDirectory()) {
				files = file.listFiles();
			}
			else {
				files = new File[1];
				files[0] = file;
			}
			for(File newFile: files) {
				if(newFile.isFile()) {
					fileName = newFile.getName();
					handler = getHandler(fileName);
					if(handler == null) {
						System.out.println("Nije moguce indeksirati dokument sa nazivom: " + fileName);
						continue;
					}
					this.indexWriter.addDocument(handler.getDocument(newFile));
				}
			}
			this.indexWriter.commit();
			System.out.println("Indexing done");			
		}
		catch (IOException e) {
			System.out.println("Indexing NOT done");
		}
	}
	
	protected DocumentHandler getHandler(String fileName) {
		switch (fileName.substring(fileName.lastIndexOf(".") + 1)) {
		case "txt":
			return new TextDocHandler();
		case "pdf":
			return new PDFHandler();
		case "doc":
			return new WordHandler();
		case "docx":
			return new Word2007Handler();
		case "xml":
			System.out.println("XML HANDLER NOT IMPLEMENTED");
		}
		return null;
	}
}
