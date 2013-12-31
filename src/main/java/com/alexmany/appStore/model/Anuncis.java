package com.alexmany.appStore.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.google.gson.annotations.Expose;


@Entity
@Table(name = "ANUNCIS")
public class Anuncis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	@Expose
	private String titol;

	private String descripcio;

	private String preu;
	
	private Users user;
	
	private Date dataCreacio;
	
	private String Estat;
	
	private List<ImageAnunci> imagesAnunci = new ArrayList<ImageAnunci>();


	// CONSTRUCTORS
	public Anuncis(String titol, String descripcio, String preu) {
		super();
		this.titol = titol;
		this.descripcio = descripcio;
		this.preu = preu;
	}
	
	

	public Anuncis() {
	}



	// GETTERS i SETTERS
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ANUNCI_ID", unique = true, nullable = false)
	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	
	@Column(name = "TITOL", unique = false, nullable = false, length = 345)
	public String getTitol() {
		return titol;
	}

	public void setTitol(String titol) {
		this.titol = titol;
	}

	@Column(name = "DESCRIPCIO", unique = false, nullable = false, length = 345)
	public String getDescripcio() {
		return descripcio;
	}

	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}

	@Column(name = "PREU", unique = false, nullable = false, length = 45)
	public String getPreu() {
		return preu;
	}

	public void setPreu(String preu) {
		this.preu = preu;
	}

	@ManyToOne
	@Cascade({CascadeType.SAVE_UPDATE})
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "anunci")
	@Cascade({CascadeType.SAVE_UPDATE})
	public List<ImageAnunci> getImagesAnunci() {
		return imagesAnunci;
	}

	public void setImagesAnunci(List<ImageAnunci> imagesAnunci) {
		this.imagesAnunci = imagesAnunci;
	}

	
	
	@Column(name = "DATA_CREACIO", unique = false, nullable = false)
	public Date getDataCreacio() {
		return dataCreacio;
	}



	public void setDataCreacio(Date dataCreacio) {
		this.dataCreacio = dataCreacio;
	}

	

	@Column(name = "ESTAT", unique = false, nullable = false)
	public String getEstat() {
		return Estat;
	}



	public void setEstat(String estat) {
		Estat = estat;
	}



	@Override
	public boolean equals(Object obj) {
		
		Anuncis anunci = (Anuncis) obj;
		boolean isequal= false;
		
		if(anunci.getId()!=null && this.id!=null){
			if(anunci.getId().equals(this.id)){
				isequal = true;
			}
		}else{
			if(anunci.getTitol()!=null && this.titol!=null){
				if(anunci.getTitol().equals(this.titol)){
					isequal = true;
				}
			}
		}
		return isequal;
	}
}
