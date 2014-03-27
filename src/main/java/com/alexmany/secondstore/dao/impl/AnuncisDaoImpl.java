package com.alexmany.secondstore.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.alexmany.secondstore.dao.AnuncisDao;
import com.alexmany.secondstore.exceptions.DAOException;
import com.alexmany.secondstore.model.Anuncis;

public class AnuncisDaoImpl extends HibernateDaoSupport implements AnuncisDao {

	/**
	 * Save an Anunci
	 * @param Anunci to be saved.
	 * @return identifier or null if anunci already exist
	 */
	public Long save(Anuncis anunci) {
		Long idAnunci=null;
		try {
			if (anunci.getId() == null) {
				idAnunci = (Long) getHibernateTemplate().save(anunci);
			}
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		return idAnunci;

	}

	/**
	 * @param anunci to update
	 */
	public void update(Anuncis anunci) {

		getHibernateTemplate().update(anunci);
	}

	/**
	 * @param anunci to delete
	 */
	public void delete(Anuncis anunci) {

		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		Anuncis anunciToDelete = (Anuncis) session.load(Anuncis.class,
				anunci.getId());
		session.delete(anunciToDelete);
		session.getTransaction().commit();
		session.close();
	}

	/**
	 * @param id of anunci
	 * @return Anunci return null if not exist
	 * 
	 */
	public Anuncis load(Long id) {
		@SuppressWarnings("unchecked")
		List<Anuncis> anunciList = (List<Anuncis>) getHibernateTemplate().find(
				"from Anuncis a where a.id = ?", id);
		if (anunciList.isEmpty())
			return null;
		Anuncis anunciFound = anunciList.get(0);
		return anunciFound;
	}

	/**
	 * @Param init first anunci in list
	 * @return list of anuncis
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Anuncis> getAll(int init) {


		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		List<Anuncis> anuncis = (List<Anuncis>) session.createQuery("from Anuncis a ").setCacheable(true).setFirstResult(init).setMaxResults(20).list();		
		session.close();
		
		return anuncis;
	}
	
	/**
	 * @return all anuncis
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Anuncis> getAll() {


		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		List<Anuncis> anuncis = (List<Anuncis>) session.createQuery("from Anuncis a ").list();		
		session.close();
		
		return anuncis;
	}
	
	/**
	 * @param init first anunci in the list
	 * @param distance max distance of anunci from lat and lot coordenades
	 * @Param lon longitud
	 * @param lat latitud
	 */
	@SuppressWarnings("unchecked")
	public List<Anuncis> search(Integer init, Integer distance, Float lat, Float lon){
		
		List<Anuncis> anuncis = new ArrayList<Anuncis>();
		
		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		//session.createCriteria(Anuncis.class).add(Restrictions.le(propertyName, value))
		anuncis =  session.createQuery("FROM Anuncis anunci where ( 6371 * acos( cos( radians(56.467056) ) * cos( radians( anunci.latitud ) ) * cos( radians( anunci.longitud ) - radians(-2.976094) ) + sin( radians(56.467056) ) * sin( radians( anunci.latitud ) ) ) ) < "+distance).setFirstResult(init).setMaxResults(20).setCacheable(true).list();		
		session.close();		
		
		return anuncis;
	}
	
	/**
	 * Search anuncis
	 * 
	 * @param titol of anunci
	 * @param userId
	 */
	@SuppressWarnings("unchecked")
	public List<Anuncis> searchBy(String titol,Long userId){
		
		List<Anuncis> anuncis = new ArrayList<Anuncis>();
		
		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		anuncis = session.createCriteria(Anuncis.class).createAlias("user", "user").add(Restrictions.eq("titol",titol)).add(Restrictions.eq("user.id", userId)).list();
		session.close();		
		
		return anuncis;
	}
	
	/**
	 * Search anuncis
	 * 
	 * @param userId
	 */
	@SuppressWarnings("unchecked")
	public List<Anuncis> searchBy(Long userId){
		
		List<Anuncis> anuncis = new ArrayList<Anuncis>();
		
		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		anuncis = session.createCriteria(Anuncis.class).createAlias("user", "user").add(Restrictions.eq("user.id", userId)).list();
		session.close();		
		
		return anuncis;
	}
}
