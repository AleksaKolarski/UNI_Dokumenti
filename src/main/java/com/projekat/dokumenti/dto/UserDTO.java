package com.projekat.dokumenti.dto;

import com.projekat.dokumenti.entity.User;

public class UserDTO {

	private Integer id;
	private String firstname;
	private String lastname;
	private String username;
	//private String password;
	private String type;
	
	
	public UserDTO() {}
	
	public UserDTO(Integer id, String firstname, String lastname, String username, String type) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.type = type;
	}
	
	public UserDTO(User user) {
		this(user.getId(), user.getFirstname(), user.getLastname(), user.getUsername(), user.getType());
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	@Override
	public String toString() {
		return "User ["
				+ "id=" + id + ", "
				+ "firstname=" + firstname + ", "
				+ "lastname=" + lastname + ", "
				+ "username=" + username + ", "
				//+ "password=" + password + ", "
				+ "type=" + type
				+ "]";
	}
}
