package com.projekat.dokumenti.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.projekat.dokumenti.security.Util;
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
	
	@Autowired
	private Util util;
		
	
	@GetMapping("/currentUser")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<User> getCurrentUser() {
		User user = util.getCurrentUser();
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
				
		String firstname = userDTO.getFirstname();
		String lastname = userDTO.getLastname();
		String username = userDTO.getUsername();
		String password = userDTO.getPassword();
		Boolean isAdmin = userDTO.getIsAdmin();
		
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
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleService.findByName("ROLE_USER"));
		User currentUser = util.getCurrentUser();
		if(currentUser != null) {
			if(isAdmin == true && currentUser.getIsAdmin()) {
				roles.add(roleService.findByName("ROLE_ADMIN"));
			}
		}
		user.setRoles(roles);
		
		userService.save(user);
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserDTO> edit(@RequestBody UserDTO userDTO){
		User currentUser = util.getCurrentUser();
		if(currentUser == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		if(userDTO.getId() != currentUser.getId() && !currentUser.checkRole("ROLE_ADMIN")) {
			// menja tudji profil a nije admin
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		String firstname = userDTO.getFirstname();
		String lastname = userDTO.getLastname();
		String username = userDTO.getUsername();
		boolean isAdmin = userDTO.getIsAdmin();
		
		if(firstname == null || firstname.length() < 5 || firstname.length() > 30) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if(lastname == null || lastname.length() < 5 || lastname.length() > 30) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if(username == null || username.length() < 5 || username.length() > 10) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		User editUser = userService.findById(userDTO.getId());
		
		if(editUser == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		editUser.setFirstname(firstname);
		editUser.setLastname(lastname);
		editUser.setUsername(username);
		if(isAdmin == true) {
			editUser.getRoles().add(roleService.findByName("ROLE_ADMIN"));
		}
		else {
			editUser.getRoles().remove(roleService.findByName("ROLE_ADMIN"));
		}
		
		editUser = userService.save(editUser);
		
		if(editUser == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new UserDTO(editUser), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> delete(@RequestParam("userId") Integer userId){
		User currentUser = util.getCurrentUser();
		if(currentUser == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}		
		
		if(userId != currentUser.getId() && !currentUser.checkRole("ROLE_ADMIN")) {
			// brise tudji profil a nije admin
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		User removeUser = userService.findById(userId);
		
		if(removeUser == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		userService.remove(removeUser);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/change-password", method = RequestMethod.PUT)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> change_password(@RequestParam("userId") Integer userId, @RequestParam("password") String password){
		User userEdit = userService.findById(userId);
		if(userEdit == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		User currentUser = util.getCurrentUser();
		if(currentUser == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		if(userEdit.getId() != currentUser.getId() && currentUser.getIsAdmin() == false) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		userEdit.setPassword(bCryptPasswordEncoder.encode(password));
		userService.save(userEdit);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
