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
	@PreAuthorize("hasRole('USER')")
	public List<EBookDTO> getAllEBooks(@RequestParam(name = "filterCategory", required = false) String filterCategory, @RequestParam(name = "sortDirection", required = false) String sortDirection){
		
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
				return EBookDTO.parseList(ebookService.findByCategoryNameOrderByAsc(filterCategory));
			}
			else {
				return EBookDTO.parseList(ebookService.findByCategoryNameOrderByDesc(filterCategory));
			}
		}
		else if(activeFilterCategory) {
			return EBookDTO.parseList(ebookService.findByCategoryName(filterCategory));
		}
		else if(activeSortDirection) {
			if(sortDirection.equals("ASC")) {
				return EBookDTO.parseList(ebookService.findAllOrderByTitleAsc());
			}
			else {
				return EBookDTO.parseList(ebookService.findAllOrderByTitleDesc());
			}
		}
		
		return EBookDTO.parseList(ebookService.findAll());
	}
	
	@GetMapping("/getById")
	@PreAuthorize("hasRole('USER')")
	public EBookDTO getById(@RequestParam("ebookId") Integer ebookId) {
		EBook ebook = ebookService.findById(ebookId);
		if(ebook == null) {
			return null;
		}
		return new EBookDTO(ebook);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EBookDTO> createEBook(@RequestBody(required = true) EBookDTO ebookDTO){
		
		Path path = Paths.get("upload-dir", ebookDTO.getFilename());
		if(!path.toFile().isFile()) {
			System.out.println("nije pronadjen fajl sa nazivom " + ebookDTO.getFilename());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		System.out.println(ebookDTO);
		EBook ebook = new EBook();
		ebook.setTitle(ebookDTO.getTitle());
		ebook.setAuthor(ebookDTO.getAuthor());
		ebook.setKeywords(ebookDTO.getKeywords());
		ebook.setPublicationYear(ebookDTO.getPublicationYear());
		ebook.setFilename(ebookDTO.getFilename());
		ebook.setDocumentName(ebookDTO.getDocumentName());
		//ebook.setMime(); // odluci sta s MIME
		
		Language language = languageService.findByName(ebookDTO.getLanguageName());
		if(language == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		ebook.setLanguage(language);
		
		Category category = categoryService.findByName(ebookDTO.getCategoryName());
		if(category == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		ebook.setCategory(category);
		
		User user = util.getCurrentUser();
		if(user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		ebook.setUser(user);
		
		
		ebook = ebookService.save(ebook);
		
		if(ebook == null) {
			logger.info("Greska pri cuvanju ebook-a, verovatno vec postoji ebook sa istim nazivom");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new EBookDTO(ebook), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EBookDTO> edit(@RequestBody(required = true) EBookDTO ebookDTO){
		
		EBook ebook;
		
		Integer id = ebookDTO.getId();
		
		if(id == null) {
			logger.info("id = null");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		ebook = ebookService.findById(id);
		
		if(ebook == null) {
			logger.info("ebook = null");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		String title = ebookDTO.getTitle();
		String author = ebookDTO.getAuthor();
		String keywords = ebookDTO.getKeywords();
		Integer publicationYear = ebookDTO.getPublicationYear();
		String languageName = ebookDTO.getLanguageName();
		String categoryName = ebookDTO.getCategoryName();
		
		if(title == null || title.length() < 5 || title.length() > 80) {
			logger.info("title not good");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(author == null || author.length() < 5 || author.length() > 120) {
			logger.info("author not good");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(keywords == null || keywords.length() < 5 || keywords.length() > 120) {
			logger.info("keywords not good");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(publicationYear == null || publicationYear < 0 || publicationYear > 2099) {
			logger.info("publicationYear not good");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(languageName == null || categoryName == null) {
			logger.info("languageName or categoryName not good");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Language language = languageService.findByName(languageName);
		Category category = categoryService.findByName(categoryName);
		
		if(language == null || category == null) {
			logger.info("language or category not good");
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
			logger.info("could not save ebook");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new EBookDTO(ebook), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> delete(@RequestParam("ebookId") Integer ebookId){
		
		if(ebookId == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		EBook ebook = ebookService.findById(ebookId);
		
		if(ebook == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		storageService.delete(ebook.getFilename());
		
		ebookService.remove(ebook);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
