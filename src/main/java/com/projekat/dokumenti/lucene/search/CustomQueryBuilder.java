package com.projekat.dokumenti.lucene.search;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;

import com.projekat.dokumenti.lucene.analysers.SerbianAnalyzer;

public class CustomQueryBuilder {

	public static Query buildQuery(SearchType searchType, String field1, String value1, String field2, String value2, String boolOperation, Integer fuzzyPhraseParam) {

		if (field1 == null || field1.equals("")) {
			return null;
		}
		if (value1 == null) {
			return null;
		}

		QueryParser queryParser = new QueryParser(Version.LUCENE_40, field1, new SerbianAnalyzer());
		AnalyzingQueryParser analyzingQueryParser = new AnalyzingQueryParser(Version.LUCENE_40, field1, new SerbianAnalyzer());
		try {
			switch (searchType) {
			case Term:
				System.out.println("\nTermQuery");
				TermQuery termQuery = new TermQuery(new Term(field1, value1));
				System.out.println("Query (preParse): " + termQuery);
				System.out.println("Query.toString(field1): " + termQuery.toString(field1));
				System.out.println("Query (afterParse): " + queryParser.parse(termQuery.toString(field1)));
				return queryParser.parse(termQuery.toString(field1));
			case Phrase:
				System.out.println("\nPhraseQuery");
				String[] words = value1.split(" ");
				PhraseQuery phraseQuery = new PhraseQuery();
				phraseQuery.setSlop(fuzzyPhraseParam);
				for (String word : words) {
					phraseQuery.add(new Term(field1, word));
				}
				System.out.println("Query (preParse): " + phraseQuery);
				System.out.println("Query.toString(field1): " + phraseQuery.toString(field1));
				System.out.println("Query (afterParse): " + queryParser.parse(phraseQuery.toString(field1)));
				return queryParser.parse(phraseQuery.toString(field1));
			case Boolean:
				System.out.println("\nBooleanQuery");
				TermQuery query1 = new TermQuery(new Term(field1, value1));
				TermQuery query2 = new TermQuery(new Term(field2, value2));
				BooleanQuery booleanQuery = new BooleanQuery();
				if (boolOperation.equalsIgnoreCase("AND")) {
					booleanQuery.add(query1, BooleanClause.Occur.MUST);
					booleanQuery.add(query2, BooleanClause.Occur.MUST);
				} else if (boolOperation.equalsIgnoreCase("OR")) {
					booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
					booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
				} else if (boolOperation.equalsIgnoreCase("NOT")) {
					booleanQuery.add(query1, BooleanClause.Occur.MUST);
					booleanQuery.add(query2, BooleanClause.Occur.MUST_NOT);
				}
				System.out.println("Query (preParse): " + booleanQuery);
				System.out.println("Query.toString(field1): " + booleanQuery.toString(field1));
				System.out.println("Query (afterParse): " + queryParser.parse(booleanQuery.toString()));
				return queryParser.parse(booleanQuery.toString());
			case Fuzzy:
				System.out.println("\nFuzzyQuery");
				FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(field1, value1), fuzzyPhraseParam);
				System.out.println("Query (preParse): " + fuzzyQuery);
				System.out.println("Query.toString(field1): " + fuzzyQuery.toString(field1));
				System.out.println("Query (afterParse): " + analyzingQueryParser.parse(fuzzyQuery.toString(field1)));
				return analyzingQueryParser.parse(fuzzyQuery.toString(field1));
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
