package com.projekat.dokumenti.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.storage.StorageService;

@Controller
@RequestMapping("/upload")
public class UploadController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	private final StorageService storageService;
	
	@Autowired
    public UploadController(StorageService storageService) {
        this.storageService = storageService;
    }
	
	@PostMapping("/")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> upload(@RequestParam("doc") MultipartFile file){
		storageService.store(file);
		System.out.println("Uploaded " + file.getOriginalFilename());
		
		// RETURN INFO FROM FILE TO SO WE CAN FILL OUT FORM ON FRONTEND
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
