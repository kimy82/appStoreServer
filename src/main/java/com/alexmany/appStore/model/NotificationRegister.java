package com.alexmany.appStore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity to store registration ids
 * 
 * @author kim
 *
 */
@Entity
@Table(name = "REGISTER")
public class NotificationRegister {

	private Long id;
	private String registerId;
	
	
	
	public NotificationRegister(String registerId) {
		this.registerId = registerId;
	}
	public NotificationRegister() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "REGISTER_ID", unique = true, nullable = false, length = 255)
	public String getRegisterId() {
		return registerId;
	}
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}
	
	
	
}
