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
			indexDir = new SimpleFSDirectory(new File(path));
			indexWriter = new IndexWriter(this.indexDir, iwc);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Path not correct");
		}
		
	}
	
	private Indexer(boolean restart) {
		this(ResourceBundle.getBundle("luceneindex").getString("indexDir"), restart);
	}
	
	private Indexer() {
		this(true);
	}
	
	public boolean add(Document doc) {
		try {
			synchronized (this) {
				indexWriter.addDocument(doc);
				indexWriter.commit();
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
				indexWriter.deleteDocuments(delTerm);
				indexWriter.commit();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public boolean index(File file) {
		try {
			Document doc = DocumentHandler.getDocument(file);
			if(doc == null) {
				System.out.println("Nije moguce indeksirati dokument sa nazivom: " + file.getName());
				return false;
			}
			indexWriter.addDocument(doc);
			indexWriter.commit();
			System.out.println("Indexing done");
			return true;
		}
		catch (IOException e) {
			System.out.println("Indexing NOT done");
		}
		return false;
	}
}
