package com.projekat.dokumenti.lucene.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;

import com.projekat.dokumenti.lucene.analysers.SerbianAnalyzer;

public class QueryBuilder {
	
	private static SerbianAnalyzer analyzer = new SerbianAnalyzer();
		
	public static Query buildQuery(SearchType searchType, String field, String value) {
		
		QueryParser queryParser = new QueryParser(Version.LUCENE_40, field, analyzer);
		
		if(field == null || field.equals("")) {
			return null;
		}
		if(value == null) {
			return null;
		}
		
		Query query = null;
		switch (searchType) {
		case Normal:
			Term term = new Term(field, value);
			query = new TermQuery(term);
			break;

		default:
			break;
		}
		
		try {
			return queryParser.parse(query.toString(field));
		} catch (ParseException e) {
			System.out.println("could not parse query");
			return null;
		}
	}
}
