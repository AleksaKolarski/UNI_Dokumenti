package com.projekat.dokumenti.service;

import java.util.List;

import com.projekat.dokumenti.entity.Role;

public interface RoleServiceInterface {
	
	List<Role> findAll();
	
	Role findByName(String name);
	
	Role findById(Integer id);
	
	List<Role> findByUserId(Integer userId);
	
	Role save(Role role);
	
	void remove(Role role);
}
