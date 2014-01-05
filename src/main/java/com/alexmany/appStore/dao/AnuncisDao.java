package com.alexmany.appStore.dao;

import java.util.List;

import com.alexmany.appStore.model.Anuncis;
import com.alexmany.appStore.pojos.AnuncisTOSearch;


public interface AnuncisDao {
	
	Long save(Anuncis anunci) ;
	
	void update(Anuncis anunci);
	
	void delete(Anuncis anunci);
	
	List<Anuncis> getAll(int init);
	
	Anuncis load( Long id );
	
	List<Anuncis> getAll();
	
	List<Anuncis> search(Integer init, Integer distance, Float lat, Float lon);
	
}
