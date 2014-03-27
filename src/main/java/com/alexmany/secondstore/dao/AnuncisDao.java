package com.alexmany.secondstore.dao;

import java.util.List;

import com.alexmany.secondstore.model.Anuncis;


public interface AnuncisDao {
	
	Long save(Anuncis anunci) ;
	
	void update(Anuncis anunci);
	
	void delete(Anuncis anunci);
	
	List<Anuncis> getAll(int init);
	
	Anuncis load( Long id );
	
	List<Anuncis> getAll();
	
	List<Anuncis> search(Integer init, Integer distance, Float lat, Float lon);
	
	List<Anuncis> searchBy(String titol,Long userId);
	
	List<Anuncis> searchBy(Long userId);
	
}
