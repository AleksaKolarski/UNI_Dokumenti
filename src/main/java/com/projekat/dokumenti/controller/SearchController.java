package com.projekat.dokumenti.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.dto.EBookDTO;
import com.projekat.dokumenti.dto.ResultDataDTO;
import com.projekat.dokumenti.entity.EBook;
import com.projekat.dokumenti.lucene.search.CustomQueryBuilder;
import com.projekat.dokumenti.lucene.search.ResultData;
import com.projekat.dokumenti.lucene.search.ResultRetriever;
import com.projekat.dokumenti.service.EBookService;
import com.projekat.dokumenti.lucene.search.SearchType;

@RestController
@RequestMapping("/search")
public class SearchController {

	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	
	@Autowired
	private EBookService ebookService;
	
	@GetMapping("/search")
	public ResponseEntity<List<ResultDataDTO>> search(	@RequestParam("searchType") SearchType searchType, 
													@RequestParam("searchTarget1") String field1, 
													@RequestParam("searchParam1") String value1, 
													@RequestParam("searchTarget2") String field2,
													@RequestParam("searchParam2") String value2,
													@RequestParam("booleanOperation") String booleanOperation){
		
		System.out.println(	  "searchType: "+ searchType +
							"\nsearchTarget1: "+ field1 +
							"\nsearchParam1: " + value1 + 
							"\nsearchTarget2: " + field2 + 
							"\nsearchParam2: " + value2 + 
							"\nbooleanOperation: " + booleanOperation);
		
		if(field1.equals("") || value1.equals("")) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}
		if(searchType == SearchType.Boolean && (field2.equals("") || value2.equals("") || booleanOperation.equals(""))) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}
		
		Query query = CustomQueryBuilder.buildQuery(searchType, field1, value1, field2, value2, booleanOperation);
		
		List<ResultData> resultData = ResultRetriever.getResults(query);
		
		List<ResultDataDTO> resultDataDTOList = new ArrayList<>();
		for(ResultData result: resultData) {
			EBook ebook = ebookService.findByFilename(result.getFilename());
			if(ebook == null) {
				continue;
			}
			ResultDataDTO resultDataDTO = new ResultDataDTO(new EBookDTO(ebook), result.getHighlight());
			resultDataDTOList.add(resultDataDTO);
		}
		
		return new ResponseEntity<>(resultDataDTOList, HttpStatus.OK);
	}
	
}
