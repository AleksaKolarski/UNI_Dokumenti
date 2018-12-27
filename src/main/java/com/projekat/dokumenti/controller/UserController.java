package com.projekat.dokumenti.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.dto.UserDTO;
import com.projekat.dokumenti.entity.User;
import com.projekat.dokumenti.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@GetMapping("/currentUser")
	public User getCurrentUser() {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		if(currentUser == null) {
			User user = new User();
			user.setFirstname("govno");
			return user;
		}
		String userUsername = currentUser.getName();
		return userService.findByUsername(userUsername);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/test")
	public String test() {
		return "ti si admin";
	}

	//@PostMapping("/register")
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
		if(userDTO == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		System.out.println(userDTO);
		
		String firstname = userDTO.getFirstname();
		String lastname = userDTO.getLastname();
		String username = userDTO.getUsername();
		String password = userDTO.getPassword();
		
		if(firstname == null || firstname.length() < 5 || firstname.length() > 30) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		if(lastname == null || lastname.length() < 5 || lastname.length() > 30) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		if(username == null || username.length() < 5 || username.length() > 10) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		if(password == null || password.length() < 5) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		if(userService.findByUsername(username) != null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		User user = new User();
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setUsername(username);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		
		userService.save(user);
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.CREATED);
	}
}
