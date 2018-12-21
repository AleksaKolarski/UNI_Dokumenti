package com.projekat.dokumenti;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DokumentiApplication {

	private static Logger log = LogManager.getLogger(DokumentiApplication.class);

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {

		log.info("Pokrenuta aplikacija");
		SpringApplication.run(DokumentiApplication.class, args);
	}
}
