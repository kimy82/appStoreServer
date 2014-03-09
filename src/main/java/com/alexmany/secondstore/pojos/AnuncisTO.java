package com.alexmany.secondstore.pojos;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 
 * 
 * @author kim
 *
 */
public class AnuncisTO extends AnuncisTOSearch{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	private Float				latitud;
	
	@Expose
	private Float				longitud;
	
	@Expose
	private List<String>        images;
	

	// CONSTRUCTORS
	public AnuncisTO(Float latitud, Float longitud,Long id, String preu, String estat,
			Double distance, String titol, String name,String descripcio, String city) {
		super(id,preu,estat,distance,titol,name,descripcio,city);
		this.latitud = latitud;
		this.longitud = longitud;
		
	}

	public Float getLatitud() {
		return latitud;
	}

	public void setLatitud(Float latitud) {
		this.latitud = latitud;
	}

	public Float getLongitud() {
		return longitud;
	}

	public void setLongitud(Float longitud) {
		this.longitud = longitud;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
	
	
	
}
