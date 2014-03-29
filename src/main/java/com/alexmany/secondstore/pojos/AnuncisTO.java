package com.alexmany.secondstore.pojos;

import java.util.List;

import com.alexmany.secondstore.model.Anuncis;
import com.google.gson.annotations.Expose;

/**
 * 
 * 
 * @author kim
 * 
 */
public class AnuncisTO extends AnuncisTOSearch {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	private Float			 latitud;

	@Expose
	private Float			 longitud;

	@Expose
	private List<String>	 images;

	@Expose
	private String			 username;

	@Expose
	private int				 numProducts;

	@Expose
	private int				 numVentas;
	
	@Expose
	private Long			 userAnunciId;
	
	@Expose
	private Long			 userId;


	// CONSTRUCTORS
	public AnuncisTO(Float latitud, Float longitud, Long id, String preu,
			String estat, Double distance, String titol, String name,
			String descripcio, String city, String userName, int numProducts, 
			int numVentas, Long userAnunciId) {
		super(id, preu, estat, distance, titol, name, descripcio, city);
		this.latitud = latitud;
		this.longitud = longitud;
		this.username = userName;
		this.numProducts = numProducts;
		this.numVentas = numVentas;
		this.userAnunciId = userAnunciId;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getNumProducts() {
		return numProducts;
	}

	public void setNumProducts(int numProducts) {
		this.numProducts = numProducts;
	}

	public int getNumVentas() {
		return numVentas;
	}

	public void setNumVentas(int numVentas) {
		this.numVentas = numVentas;
	}

	public Long getUserAnunciId() {
		return userAnunciId;
	}

	public void setUserAnunciId(Long userAnunciId) {
		this.userAnunciId = userAnunciId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
	

}
