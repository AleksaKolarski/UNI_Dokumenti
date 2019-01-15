package com.projekat.dokumenti.lucene.search;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import com.projekat.dokumenti.lucene.analysers.SerbianAnalyzer;
import com.projekat.dokumenti.parser.CustomDocumentParser;
import com.projekat.dokumenti.parser.CustomParsedDocument;

public class ResultRetriever {
	
	public static List<ResultData> getResults(Query query){
		if(query == null) {
			return null;
		}
		List<ResultData> results = new ArrayList<ResultData>();
		
		try {
			File path = new File(ResourceBundle.getBundle("luceneindex").getString("indexDir"));
			Directory indexDir = new SimpleFSDirectory(path);
			DirectoryReader reader = DirectoryReader.open(indexDir);
			IndexSearcher is = new IndexSearcher(reader);
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
								
				String highlight = "";
				
				hl = new Highlighter(new QueryScorer(query, reader, "text"));
				
				highlight += hl.getBestFragment(sa, "text", "" + getDocumentText(filename));
				
				rd = new ResultData(filename, highlight);
				results.add(rd);
			}
			
			reader.close();
		} catch (Exception e) {
			return null;
		}
		
		return results;
	}
	
	private static String getDocumentText(String filename) {
		Path path = Paths.get("upload-dir", filename);
		File file = new File(path.toString());
		CustomParsedDocument parsedDocument = CustomDocumentParser.parseDocument(file);
		return parsedDocument.getText();
	}
}
