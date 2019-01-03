package com.projekat.dokumenti.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.entity.Role;
import com.projekat.dokumenti.service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {
	
	private final Logger logger = LogManager.getLogger(DokumentiApplication.class);
	
	@Autowired
	private RoleService roleService;
	
	// getById
	@GetMapping("/getById")
	public Role getById(@RequestParam("roleId") Integer roleId) {
		return roleService.findById(roleId);
	}
	
	// getAll
	@GetMapping("/getAll")
	public List<Role> getAll(){
		return roleService.findAll();
	}
	
	// getAllByUserId
	@GetMapping("/getAllByUserId")
	public List<Role> getAllByUserId(@RequestParam("userId") Integer userId){
		return roleService.findByUserId(userId);
	}
}
