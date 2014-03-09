package com.alexmany.secondstore.pojos;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class AnuncisTOSearch implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	private Long				id;
	
	@Expose
	private String				name;
	
	@Expose
	private String				preu;
	
	@Expose
	private String				estat;
	
	@Expose
	private Double				distance;
	
	@Expose
	private String				titol;
	
	@Expose
	private String 				descripcio;
	
	@Expose
	private String 				city;
	
	
	

	public AnuncisTOSearch(Long id, String preu, String estat,
			Double distance, String titol, String name,String descripcio, String city) {
		super();
		this.id = id;
		this.preu = preu;
		this.estat = estat;
		this.distance = distance;
		this.titol = titol;
		this.name = name;
		this.descripcio = descripcio;
		this.city = city;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPreu() {
		return preu;
	}

	public void setPreu(String preu) {
		this.preu = preu;
	}

	public String getEstat() {
		return estat;
	}

	public void setEstat(String estat) {
		this.estat = estat;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getTitol() {
		return titol;
	}

	public void setTitol(String titol) {
		this.titol = titol;
	}

	public String getDescripcio() {
		return descripcio;
	}

	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
}
