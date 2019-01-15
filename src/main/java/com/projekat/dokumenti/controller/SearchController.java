package com.projekat.dokumenti.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.dto.EBookDTO;
import com.projekat.dokumenti.lucene.search.QueryBuilder;
import com.projekat.dokumenti.lucene.search.ResultData;
import com.projekat.dokumenti.lucene.search.ResultRetriever;
import com.projekat.dokumenti.lucene.search.SearchType;

@RestController
@RequestMapping("/search")
public class SearchController {

	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	@GetMapping("/search")
	public ResponseEntity<List<ResultData>> search(@RequestParam("searchType") SearchType searchType, @RequestParam("normalSearchTarget") String field, @RequestParam("normalSearchTargetValue") String value){
		
		System.out.println("searchType: "+ searchType +"\nnormalSearchTarget: "+ field +"\nnormalSearchTargetValue: " + value);
		
		Query query = QueryBuilder.buildQuery(searchType, "text", value);
		
		System.out.println("Query: " + query);
		
		List<ResultData> resultData = ResultRetriever.getResults(query);
		
		return new ResponseEntity<>(resultData, HttpStatus.OK);
	}
	
}
