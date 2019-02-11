package com.projekat.dokumenti.lucene.index;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import com.projekat.dokumenti.entity.EBook;
import com.projekat.dokumenti.lucene.analysers.SerbianAnalyzer;
import com.projekat.dokumenti.lucene.search.ResultRetriever;
import com.projekat.dokumenti.parser.CustomDocumentParser;

public class Indexer {
	
	private IndexWriter indexWriter;
	private Directory indexDir;
	
	private static Indexer indexer = new Indexer(false);
	
	public static Indexer getInstance() {
		return indexer;
	}
	

	private Indexer(boolean restart) {
		String path = ResourceBundle.getBundle("luceneindex").getString("indexDir");
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, new SerbianAnalyzer());
		if(restart) {
			iwc.setOpenMode(OpenMode.CREATE);
		}
		else {
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		
		try {
			indexDir = new SimpleFSDirectory(Paths.get(path).toFile());
			indexWriter = new IndexWriter(this.indexDir, iwc);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Path not correct");
		}
		
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
	
	public boolean delete(EBook ebook){
		if(ebook == null) {
			return false;
		}
		Term delTerm = new Term("filename", ebook.getFilename());
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
	
	public boolean update(EBook ebook) {
		
		if(ebook == null) {
			return false;
		}
		
		Document doc = ResultRetriever.getDocumentByFilename(ebook.getFilename());
		if(doc == null) {
			return false;
		}
		
		List<IndexableField> fields = new ArrayList<IndexableField>();
		
		fields.add(new IntField("id", ebook.getId(), Store.YES));
		fields.add(new TextField("title", ebook.getTitle(), Store.YES));
		fields.add(new TextField("author", ebook.getAuthor(), Store.YES));
		String[] splittedKeywords = ebook.getKeywords().split("[;, ]+");
		for(String keyword: splittedKeywords) {
			fields.add(new TextField("keyword", keyword, Store.YES));
		}
		fields.add(new IntField("publicationYear", ebook.getPublicationYear(), Store.YES));
		fields.add(new TextField("language", ebook.getLanguage().getName(), Store.YES));
		fields.add(new StringField("filename", ebook.getFilename(), Store.YES));
		fields.add(new TextField("text", CustomDocumentParser.parseDocumentText(new File(Paths.get("upload-dir", ebook.getFilename()).toString())), Store.NO));
		
		for(IndexableField field: fields) {
			doc.removeFields(field.name());
		}
		for(IndexableField field: fields) {
			doc.add(field);
		}
		try {
			indexWriter.updateDocument(new Term("filename", ebook.getFilename()), doc);
			indexWriter.commit();
			return true;
		}
		catch (Exception e) {
		}
		return false;
	}
	
	public boolean index(EBook ebook) {
		File file = new File(Paths.get("upload-dir", ebook.getFilename()).toString());
		try {
			Document doc = DocumentHandler.getDocument(file, ebook);
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
