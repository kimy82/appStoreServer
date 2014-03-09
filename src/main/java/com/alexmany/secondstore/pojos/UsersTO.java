package com.alexmany.secondstore.pojos;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class UsersTO implements Serializable{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	
	@Expose
	private String				username;
	
	@Expose
	private String				role;

	// CONSTRUCTORS
	public UsersTO() {

		super();
	}
	
	

	public UsersTO( String username, String role ) {

		super();
		this.username = username;
		this.role = role;
	}



	public String getUsername(){
	
		return username;
	}

	public void setUsername( String username ){
	
		this.username = username;
	}

	public String getRole(){
	
		return role;
	}

	public void setRole( String role ){
	
		this.role = role;
	}

	

	}
