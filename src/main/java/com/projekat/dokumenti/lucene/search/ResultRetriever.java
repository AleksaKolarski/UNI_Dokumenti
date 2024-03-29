package com.projekat.dokumenti.lucene.search;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.stereotype.Component;

import com.projekat.dokumenti.lucene.analysers.SerbianAnalyzer;
import com.projekat.dokumenti.parser.CustomDocumentParser;

@Component
public class ResultRetriever {
	
	private static File file = new File(ResourceBundle.getBundle("luceneindex").getString("indexDir"));
	private static Directory indexDir;
	private static DirectoryReader reader;
	private static IndexSearcher is;
	
	
	public static List<ResultData> getResults(Query query){
		if(query == null) {
			return null;
		}
		List<ResultData> results = new ArrayList<ResultData>();
		
		try {
			
			if(!file.exists()) {
				file.mkdir();
			}
			indexDir = new SimpleFSDirectory(file);
			reader = DirectoryReader.open(indexDir);
			is = new IndexSearcher(reader);
			
			TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
			is.search(query, collector);
			
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			ResultData rd;
			Document doc;
			Highlighter hl;
			SerbianAnalyzer sa = new SerbianAnalyzer();
			
			for(ScoreDoc sd: hits) {
				doc = is.doc(sd.doc);
				String filename = doc.get("filename");
								
				hl = new Highlighter(new QueryScorer(query, reader, "text"));
				
				String docText = getDocumentText(filename);
				String highlight = hl.getBestFragment(sa, "text", docText);
				
				if(highlight == null) {
					highlight = docText.substring(0, 120);
				}
				
				rd = new ResultData(filename, highlight);
				results.add(rd);
			}
			
		} catch (Exception e) {
		}
		
		return results;
	}
	
	public static Document getDocumentByFilename(String filename){
		try {
			
			if(!file.exists()) {
				file.mkdir();
			}
			indexDir = new SimpleFSDirectory(file);
			reader = DirectoryReader.open(indexDir);
			is = new IndexSearcher(reader);
			
			Query query = new TermQuery(new Term("filename", filename));
			TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
			is.search(query, collector);
			ScoreDoc[] scoreDocs = collector.topDocs().scoreDocs;
			if(scoreDocs.length > 0) {
				int docId = scoreDocs[0].doc;
				return is.doc(docId);
			}
		}
		catch (Exception e) {
		}
		return null;
	}
	
	public static List<Document> getAllIndexedDocuments(){
		List<Document> documents = new ArrayList<Document>();
		try {
			
			if(!file.exists()) {
				file.mkdir();
			}
			indexDir = new SimpleFSDirectory(file);
			reader = DirectoryReader.open(indexDir);
			is = new IndexSearcher(reader);
			
			Query query = new MatchAllDocsQuery();
			TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
			is.search(query, collector);
			ScoreDoc[] scoreDocs = collector.topDocs().scoreDocs;
			for(ScoreDoc sc: scoreDocs) {
				int docId = sc.doc;
				documents.add(is.doc(docId));
			}
		}
		catch (Exception e) {
		}
		return documents;
	}
	
	private static String getDocumentText(String filename) {
		Path path = Paths.get("upload-dir", filename);
		File file = new File(path.toString());
		return CustomDocumentParser.parseDocumentText(file);
	}
}
