package com.alexmany.appStore.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "USERS")
public class Users implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	@Expose
	private String username;

	private String password;

	protected UserRole userRole;


	// CONSTRUCTORS
	public Users(String username) {

		super();
		this.username = username;
	}

	public Users() {

		super();
	}

	// GETTERS i SETTERS
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_ID", unique = true, nullable = false)
	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	@Column(name = "USERNAME", unique = true, nullable = false, length = 45)
	public String getUsername() {

		return username;
	}

	public void setUsername(String username) {

		this.username = username;
	}

	@Column(name = "PASSWORD", unique = false, nullable = false, length = 65)
	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}

	@ManyToOne
	@Cascade({})
	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {

		this.userRole = userRole;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		
		Users user = (Users) obj;
		boolean isequal= false;
		
		if(user.getId()!=null && this.id!=null){
			if(user.getId().equals(this.id)){
				isequal = true;
			}
		}else{
			if(user.getUsername()!=null && this.username!=null){
				if(user.getUsername().equals(this.username)){
					isequal = true;
				}
			}
		}
		
		return isequal;
	}
}
