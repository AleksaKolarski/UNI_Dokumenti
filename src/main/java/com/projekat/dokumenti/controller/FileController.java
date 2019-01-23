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
	
	private final Logger logger = LogManager.getLogger(FileController.class);
	
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
			logger.info("/file/upload | could not store file to filesystem");
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Parsiranje metapodataka za vracanje na frontend
		CustomParsedDocument customParsedDocument = CustomDocumentParser.parseMetadata(storedFile);
		if(customParsedDocument == null) {
			FileSystemUtils.deleteRecursively(storedFile);
			logger.info("/file/upload | could not parse document metadata");
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		customParsedDocument.setDocumentName(documentFilename);
		
		return new ResponseEntity<>(customParsedDocument, HttpStatus.OK);
	}
	
	@GetMapping("/generate-token/{filename}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> generateToken(@PathVariable("filename") String filename) {
		 
		User user = util.getCurrentUser();
		
		if(user == null) {
			logger.info("/file/generate-token/{filename} | current user is not logged in");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		EBook ebook = ebookService.findByFilename(filename);
		
		if(ebook == null) {
			logger.info("/file/generate-token/{filename} | could not find ebook with filename=" + filename);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(user.getIsAdmin() == false) {
			if(user.getCategory() != null) {
				if(user.getCategory().getId() != ebook.getCategory().getId()) {
					logger.info("/file/generate-token/{filename} | current user is not subscribed to ebook category");
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
			}
		}
		
		Random random = new Random();
		Integer code = random.nextInt();
		
		fileAccessTokens.put(user.getUsername() + ":" + code, filename);
				
		return new ResponseEntity<>(user.getUsername() + ":" + code, HttpStatus.OK);
	}
	
	@GetMapping("/download/{token}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable("token") String token){
		
		String filename;
		
		filename = fileAccessTokens.get(token);
		
		if(filename == null) {
			logger.info("/file/download/{token} | token not valid");
			return new ResponseEntity<Resource>(HttpStatus.BAD_REQUEST);
		}
		
		EBook ebook = ebookService.findByFilename(filename);
		
		if(ebook == null) {
			logger.info("/file/download/{token} | could not find ebook with filename=" + filename);
			return new ResponseEntity<Resource>(HttpStatus.BAD_REQUEST);
		}
				 
		Resource file = storageService.loadAsResource(filename);
		
		fileAccessTokens.remove(token);
		
        if(ebook.getMime() == null) {
        	return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }
        else {
        	HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", ebook.getMime());
        	return new ResponseEntity<>(file, headers, HttpStatus.OK);
        }
	}
}
