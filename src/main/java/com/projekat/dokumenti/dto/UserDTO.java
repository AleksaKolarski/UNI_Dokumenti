package com.projekat.dokumenti.dto;

import java.util.ArrayList;
import java.util.List;

import com.projekat.dokumenti.entity.Category;
import com.projekat.dokumenti.entity.User;

public class UserDTO {

	private Integer id;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private String categoryName;
	private Boolean isAdmin;
	
	public UserDTO() {}
	
	public UserDTO(Integer id, String firstname, String lastname, String username, Boolean isAdmin) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.isAdmin = isAdmin;
	}
	
	public UserDTO(User user) {
		this(user.getId(), user.getFirstname(), user.getLastname(), user.getUsername(), user.getIsAdmin());
		
		Category category = user.getCategory();
		if(category != null) {
			this.categoryName = user.getCategory().getName();
		}
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "User ["
				+ "id=" + id + ", "
				+ "firstname=" + firstname + ", "
				+ "lastname=" + lastname + ", "
				+ "username=" + username + ", "
				+ "password=" + password + ", "
				+ "categoryName=" + categoryName + ", " 
				+ "isAdmin=" + isAdmin
				+ "]";
	}
	
	public static List<UserDTO> parseList(List<User> list){
		List<UserDTO> listDTO = new ArrayList<UserDTO>();
		for(User user: list) {
			listDTO.add(new UserDTO(user));
		}
		return listDTO;
	}
}
