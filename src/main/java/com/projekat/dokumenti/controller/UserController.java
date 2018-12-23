package com.projekat.dokumenti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.entity.User;
import com.projekat.dokumenti.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
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

	@PostMapping("/register")
	public void register(@RequestBody User user) {
		if(user == null) {
			// log
			return;
		}
		
		//user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userService.save(user);
	}
}
