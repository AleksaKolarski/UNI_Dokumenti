package com.projekat.dokumenti.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.dto.EBookDTO;
import com.projekat.dokumenti.entity.Category;
import com.projekat.dokumenti.entity.EBook;
import com.projekat.dokumenti.entity.Language;
import com.projekat.dokumenti.entity.User;
import com.projekat.dokumenti.lucene.index.Indexer;
import com.projekat.dokumenti.security.Util;
import com.projekat.dokumenti.service.CategoryService;
import com.projekat.dokumenti.service.EBookService;
import com.projekat.dokumenti.service.LanguageService;
import com.projekat.dokumenti.storage.FileSystemStorageService;

@RestController
@RequestMapping("/ebook")
public class EBookController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	@Autowired
	private EBookService ebookService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FileSystemStorageService storageService;
	
	@Autowired
	private Util util;
	
	@GetMapping("/all")
	public ResponseEntity<List<EBookDTO>> getAllEBooks(@RequestParam(name = "filterCategory", required = false) String filterCategory, @RequestParam(name = "sortDirection", required = false) String sortDirection){
		
		boolean activeFilterCategory = false;
		boolean activeSortDirection = false;
		
		if(filterCategory != null && filterCategory.length() > 0) {
			activeFilterCategory = true;
		}
		if(sortDirection != null && ((sortDirection.equals("ASC") || sortDirection.equals("DESC")))) {
			activeSortDirection = true;
		}
		
		// Pozive jpa repository metoda izmeniti da se koristi Pageable
		if(activeFilterCategory && activeSortDirection) {
			if(sortDirection.equals("ASC")) {
				return new ResponseEntity<>(EBookDTO.parseList(ebookService.findByCategoryNameOrderByAsc(filterCategory)), HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(EBookDTO.parseList(ebookService.findByCategoryNameOrderByDesc(filterCategory)), HttpStatus.OK);
			}
		}
		else if(activeFilterCategory) {
			return new ResponseEntity<>(EBookDTO.parseList(ebookService.findByCategoryName(filterCategory)), HttpStatus.OK);
		}
		else if(activeSortDirection) {
			if(sortDirection.equals("ASC")) {
				return new ResponseEntity<>(EBookDTO.parseList(ebookService.findAllOrderByTitleAsc()), HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(EBookDTO.parseList(ebookService.findAllOrderByTitleDesc()), HttpStatus.OK);
			}
		}
		
		return new ResponseEntity<>(EBookDTO.parseList(ebookService.findAll()), HttpStatus.OK);
	}
	
	@GetMapping("/getById")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<EBookDTO> getById(@RequestParam("ebookId") Integer ebookId) {
		EBook ebook = ebookService.findById(ebookId);
		if(ebook == null) {
			logger.info("/ebook/getById | could not find ebook with id=" + ebookId);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new EBookDTO(ebook), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EBookDTO> createEBook(@RequestBody(required = true) EBookDTO ebookDTO){
		
		String filename = ebookDTO.getFilename().replaceAll(" ", "_");
		
		Path path = Paths.get("upload-dir", filename);
		if(!path.toFile().isFile()) {
			logger.info("/ebook/create | could not find file=" + filename);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		EBook ebook = new EBook();
		ebook.setTitle(ebookDTO.getTitle());
		ebook.setAuthor(ebookDTO.getAuthor());
		ebook.setKeywords(ebookDTO.getKeywords());
		ebook.setPublicationYear(ebookDTO.getPublicationYear());
		ebook.setFilename(filename);
		ebook.setDocumentName(ebookDTO.getDocumentName());
		ebook.setMime(ebookDTO.getMime());
		
		Language language = languageService.findByName(ebookDTO.getLanguageName());
		if(language == null) {
			logger.info("/ebook/create | could not find language with name " + ebookDTO.getLanguageName());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		ebook.setLanguage(language);
		
		Category category = categoryService.findByName(ebookDTO.getCategoryName());
		if(category == null) {
			logger.info("/ebook/create | could not find category with name " + ebookDTO.getCategoryName());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		ebook.setCategory(category);
		
		User user = util.getCurrentUser();
		if(user == null) {
			logger.info("/ebook/create | current user not logged in");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		ebook.setUser(user);
		
		ebook = ebookService.save(ebook);
		
		if(ebook == null) {
			logger.info("/ebook/create | could not save ebook");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Boolean indexSuccessful;
		indexSuccessful = Indexer.getInstance().index(ebook);
		if(indexSuccessful == false) {
			logger.info("/ebook/create | could not index ebook with filename=" + ebook.getFilename());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new EBookDTO(ebook), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EBookDTO> edit(@RequestBody(required = true) EBookDTO ebookDTO){
		
		EBook ebook;
		
		Integer id = ebookDTO.getId();
		
		if(id == null) {
			logger.info("/ebook/edit | ebookDTO ID = null");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		ebook = ebookService.findById(id);
		
		if(ebook == null) {
			logger.info("/ebook/edit | could not find ebook with id=" + id);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		String title = ebookDTO.getTitle();
		String author = ebookDTO.getAuthor();
		String keywords = ebookDTO.getKeywords();
		Integer publicationYear = ebookDTO.getPublicationYear();
		String languageName = ebookDTO.getLanguageName();
		String categoryName = ebookDTO.getCategoryName();
		
		if(title == null || title.length() < 5 || title.length() > 80) {
			logger.info("/ebook/edit | title not valid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(author == null || author.length() < 5 || author.length() > 120) {
			logger.info("/ebook/edit | author not valid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(keywords == null || keywords.length() > 120) {
			logger.info("/ebook/edit | keywords not valid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(publicationYear == null || publicationYear < 0 || publicationYear > 2099) {
			logger.info("/ebook/edit | publicationYear not valid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(languageName == null || categoryName == null) {
			logger.info("/ebook/edit | languageName or categoryName not valid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Language language = languageService.findByName(languageName);
		Category category = categoryService.findByName(categoryName);
		
		if(language == null || category == null) {
			logger.info("/ebook/edit | language or category not valid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		ebook.setTitle(title);
		ebook.setAuthor(author);
		ebook.setKeywords(keywords);
		ebook.setPublicationYear(publicationYear);
		ebook.setLanguage(language);
		ebook.setCategory(category);
		
		ebook = ebookService.save(ebook);
		
		if(ebook == null) {
			logger.info("/ebook/edit | could not save ebook");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(!Indexer.getInstance().update(ebook)) {
			logger.info("/ebook/edit | could not reindex ebook with filename=" + ebook.getFilename());
		}
		
		return new ResponseEntity<>(new EBookDTO(ebook), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> delete(@RequestParam("ebookId") Integer ebookId){
		
		if(ebookId == null) {
			logger.info("/ebook/delete | ebookId not valid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		EBook ebook = ebookService.findById(ebookId);
		
		if(ebook == null) {
			logger.info("/ebook/delete | could not find ebook with id=" + ebookId);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		storageService.delete(ebook.getFilename());
		
		Indexer.getInstance().delete(ebook);
		
		ebookService.remove(ebook);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
