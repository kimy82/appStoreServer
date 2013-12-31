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
import org.hibernate.annotations.CascadeType;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "ANUNCI_IMAGE")
public class ImageAnunci implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	@Expose
	private String name;
	
	private Anuncis anunci;

	// CONSTRUCTORS
	public ImageAnunci(String name) {
		super();
		this.name = name;
	}
	
	public ImageAnunci() {
	
	}
	

	// GETTERS i SETTERS
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "IMAGE_ANUNCI_ID", unique = true, nullable = false)
	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	@Column(name = "NAME", unique = false, nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	@ManyToOne
	@Cascade({CascadeType.SAVE_UPDATE})
	public Anuncis getAnunci() {
		return anunci;
	}

	public void setAnunci(Anuncis anunci) {
		this.anunci = anunci;
	}

	@Override
	public boolean equals(Object obj) {

		ImageAnunci imageAnunci = (ImageAnunci) obj;
		boolean isequal = false;

		if (imageAnunci.getId() != null && this.id != null) {
			if (imageAnunci.getId().equals(this.id)) {
				isequal = true;
			}
		} else {
			if (imageAnunci.getName() != null && this.name != null) {
				if (imageAnunci.getName().equals(this.name)) {
					isequal = true;
				}
			}
		}
		return isequal;
	}
}
