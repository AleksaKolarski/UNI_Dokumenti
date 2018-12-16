package com.projekat.dokumenti.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "user_firstname", unique = false, nullable = false, length = 30)
	private String firstname;
	
	@Column(name = "user_lastname", unique = false, nullable = false, length = 30)
	private String lastname;
	
	@Column(name = "user_username", unique = true, nullable = false, length = 10)
	private String username;
	
	@Column(name = "user_password", unique = false, nullable = false, length = 10)
	private String password;
	
	@Column(name = "user_type", unique = false, nullable = false, length = 30)
	private String type;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<EBook> ebooks = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "category_id", unique = false, nullable = true)
	private Category category;
	
	
	public User() {}


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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<EBook> getEbooks() {
		return ebooks;
	}
	
	public void setEbooks(List<EBook> ebooks) {
		this.ebooks = ebooks;
	}
	
	@Override
	public String toString() {
		return "User ["
				+ "id=" + id + ", "
				+ "firstname=" + firstname + ", "
				+ "lastname=" + lastname + ", "
				+ "username=" + username + ", "
				+ "password=" + password + ", "
				+ "type=" + type
				+ "]";
	}
}
