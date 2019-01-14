package com.projekat.dokumenti.controller;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.entity.Role;
import com.projekat.dokumenti.entity.User;
import com.projekat.dokumenti.security.Util;
import com.projekat.dokumenti.service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private Util util;
	
	// getById
	@GetMapping("/getById")
	public ResponseEntity<Role> getById(@RequestParam("roleId") Integer roleId) {
		return new ResponseEntity<>(roleService.findById(roleId), HttpStatus.OK);
	}
	
	// getAll
	@GetMapping("/getAll")
	public ResponseEntity<List<Role>> getAll(){
		return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
	}
	
	// getAllByUserId
	@GetMapping("/getAllByUserId")
	public ResponseEntity<List<Role>> getAllByUserId(@RequestParam("userId") Integer userId){
		return new ResponseEntity<>(roleService.findByUserId(userId), HttpStatus.OK);
	}
	
	@GetMapping("/current-user-roles")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Set<Role>> getCurrentUserRoles(){
		User user = util.getCurrentUser();
		if(user == null) {
			return new ResponseEntity<Set<Role>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Set<Role>>(user.getRoles(), HttpStatus.OK);
	}
}
