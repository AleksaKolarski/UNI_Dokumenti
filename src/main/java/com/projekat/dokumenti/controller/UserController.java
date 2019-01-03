package com.projekat.dokumenti.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.dto.UserDTO;
import com.projekat.dokumenti.entity.Role;
import com.projekat.dokumenti.entity.User;
import com.projekat.dokumenti.service.RoleService;
import com.projekat.dokumenti.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@GetMapping("/currentUser")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<User> getCurrentUser(Principal principal) {
		if (principal == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		User user = userService.findByUsername(principal.getName());
		if (user == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public User getById(@RequestParam("userId") Integer userId) {
		return userService.findById(userId);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> getAllUsers(){
		return userService.findAll();
	}

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
			logger.debug("User tried to register with existing username.");
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}
		
		User user = new User();
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setUsername(username);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		List<Role> roles = new ArrayList<Role>();
		roles.add(roleService.findByName("ROLE_USER"));
		user.setRoles(roles);
		
		userService.save(user);
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserDTO> edit(@RequestBody UserDTO userDTO){
		Authentication currentUserAuth;
		String userUsername;
		User currentUser;
		
		currentUserAuth = SecurityContextHolder.getContext().getAuthentication();
		if(currentUserAuth == null) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		userUsername = currentUserAuth.getName();
		currentUser = userService.findByUsername(userUsername);
		if(currentUser == null) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		
		if(userDTO.getId() != currentUser.getId() && !currentUser.checkRole("ROLE_ADMIN")) {
			// menja tudji profil a nije admin
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		
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
		
		User editUser = userService.findById(userDTO.getId());
		
		if(editUser == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		editUser.setFirstname(firstname);
		editUser.setLastname(lastname);
		editUser.setUsername(username);
		editUser.setPassword(bCryptPasswordEncoder.encode(password));
		
		editUser = userService.save(editUser);
		
		if(editUser == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new UserDTO(editUser), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> delete(@RequestBody UserDTO userDTO){
		Authentication currentUserAuth;
		String userUsername;
		User currentUser;
		
		currentUserAuth = SecurityContextHolder.getContext().getAuthentication();
		if(currentUserAuth == null) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		userUsername = currentUserAuth.getName();
		currentUser = userService.findByUsername(userUsername);
		if(currentUser == null) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		
		if(userDTO.getId() != currentUser.getId() && !currentUser.checkRole("ROLE_ADMIN")) {
			// brise tudji profil a nije admin
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		
		User removeUser = userService.findById(userDTO.getId());
		
		if(removeUser == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		userService.remove(removeUser);
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
}
