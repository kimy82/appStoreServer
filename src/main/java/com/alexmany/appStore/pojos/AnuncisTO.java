package com.alexmany.appStore.pojos;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class AnuncisTO implements Serializable{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	
	@Expose
	private String				titol;
	
	@Expose
	private String				path;
	
	@Expose
	private String				preu;

	// CONSTRUCTORS
	public AnuncisTO() {

		super();
	}

	public AnuncisTO(String titol, String path , String preu) {
		super();
		this.titol = titol;
		this.path = path;
		this.preu = preu;
	}

	public String getTitol() {
		return titol;
	}

	public void setTitol(String titol) {
		this.titol = titol;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}	
	
}
