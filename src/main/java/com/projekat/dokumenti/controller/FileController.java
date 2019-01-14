package com.projekat.dokumenti.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.entity.EBook;
import com.projekat.dokumenti.entity.User;
import com.projekat.dokumenti.parser.CustomDocumentParser;
import com.projekat.dokumenti.parser.CustomParsedDocument;
import com.projekat.dokumenti.security.Util;
import com.projekat.dokumenti.service.EBookService;
import com.projekat.dokumenti.storage.FileSystemStorageService;

@Controller
@RequestMapping("/file")
public class FileController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	private static Map<String, String> fileAccessTokens = new HashMap<String, String>();
	
	@Autowired
	private FileSystemStorageService storageService;
	
	@Autowired
	private EBookService ebookService;
	
	@Autowired
	private Util util;
	
	@PostMapping("/upload")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CustomParsedDocument> upload(@RequestParam("doc") MultipartFile file){
		
		String documentFilename = file.getOriginalFilename();
		
		// Cuvanje fajla u upload-dir
		File storedFile = storageService.store(file);
		if(storedFile == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Parsiranje metapodataka za vracanje na frontend
		CustomParsedDocument customParsedDocument = CustomDocumentParser.parseMetadata(storedFile);
		if(customParsedDocument == null) {
			FileSystemUtils.deleteRecursively(storedFile);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		customParsedDocument.setDocumentName(documentFilename);
		
		System.out.println(customParsedDocument);
		
		return new ResponseEntity<>(customParsedDocument, HttpStatus.OK);
	}
	
	@GetMapping("/generate-token/{documentName}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> generateToken(@PathVariable("documentName") String documentName) {
		 
		User user = util.getCurrentUser();
		
		if(user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		EBook ebook = ebookService.findByDocumentName(documentName);
		
		if(ebook == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Random random = new Random();
		Integer code = random.nextInt();
		
		fileAccessTokens.put(user.getUsername() + ":" + code, ebook.getDocumentName());
				
		return new ResponseEntity<>(user.getUsername() + ":" + code, HttpStatus.OK);
	}
	
	@GetMapping("/download/{token}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable("token") String token){
		
		String documentName;
		
		documentName = fileAccessTokens.get(token);
		
		if(documentName == null) {
			return new ResponseEntity<Resource>(HttpStatus.BAD_REQUEST);
		}
		
		EBook ebook = ebookService.findByDocumentName(documentName);
		
		if(ebook == null) {
			return new ResponseEntity<Resource>(HttpStatus.BAD_REQUEST);
		}
		
		String filename = ebook.getFilename();
		 
		Resource file = storageService.loadAsResource(filename);
		
		fileAccessTokens.remove(token);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
}
