package com.projekat.dokumenti.controller;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.parser.CustomDocumentParser;
import com.projekat.dokumenti.parser.CustomParsedDocument;
import com.projekat.dokumenti.storage.FileSystemStorageService;

@Controller
@RequestMapping("/upload")
public class UploadController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	@Autowired
	private FileSystemStorageService storageService;
	
	@PostMapping("/")
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
}
