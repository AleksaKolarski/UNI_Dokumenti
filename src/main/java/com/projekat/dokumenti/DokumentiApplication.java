package com.projekat.dokumenti;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.projekat.dokumenti.storage.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class DokumentiApplication {

	private static final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	public static void main(String[] args) {

		logger.info("Pokrenuta aplikacija");
		SpringApplication.run(DokumentiApplication.class, args);
	}
}