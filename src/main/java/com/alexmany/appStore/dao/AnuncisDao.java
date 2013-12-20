package com.alexmany.appStore.dao;

import java.util.List;

import com.alexmany.appStore.model.Anuncis;


public interface AnuncisDao {
	
	void save(Anuncis anunci) ;
	
	void update(Anuncis anunci);
	
	void delete(Anuncis anunci);
	
	List<Anuncis> getAll();
	
	Anuncis load( Long id );
	
	
}
