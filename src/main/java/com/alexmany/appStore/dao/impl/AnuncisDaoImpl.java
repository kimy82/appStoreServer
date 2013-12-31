package com.alexmany.appStore.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.alexmany.appStore.dao.AnuncisDao;
import com.alexmany.appStore.exceptions.DAOException;
import com.alexmany.appStore.model.Anuncis;

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

	public void update(Anuncis anunci) {

		getHibernateTemplate().update(anunci);
	}

	public void delete(Anuncis anunci) {

		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		Anuncis anunciToDelete = (Anuncis) session.load(Anuncis.class,
				anunci.getId());
		session.delete(anunciToDelete);
		session.getTransaction().commit();
		session.close();
	}

	public Anuncis load(Long id) {
		@SuppressWarnings("unchecked")
		List<Anuncis> anunciList = (List<Anuncis>) getHibernateTemplate().find(
				"from Anuncis a where a.id = ?", id);
		if (anunciList.isEmpty())
			return null;
		Anuncis anunciFound = anunciList.get(0);
		return anunciFound;
	}

	@Transactional
	public List<Anuncis> getAll(int init) {


		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		List<Anuncis> anuncis = (List<Anuncis>) session.createQuery("from Anuncis a ").setFirstResult(init).setMaxResults(20).list();		
		session.close();
		
		return anuncis;
	}
	
	@Transactional
	public List<Anuncis> getAll() {


		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		List<Anuncis> anuncis = (List<Anuncis>) session.createQuery("from Anuncis a ").list();		
		session.close();
		
		return anuncis;
	}
}
