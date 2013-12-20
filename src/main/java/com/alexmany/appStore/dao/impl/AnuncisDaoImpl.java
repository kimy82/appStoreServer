package com.alexmany.appStore.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.alexmany.appStore.dao.AnuncisDao;
import com.alexmany.appStore.exceptions.DAOException;
import com.alexmany.appStore.model.Anuncis;

public class AnuncisDaoImpl extends HibernateDaoSupport implements AnuncisDao {

	public void save(Anuncis anunci) {
		try {
			if (anunci.getId() != null) {
				getHibernateTemplate().save(anunci);
			}
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}

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
	public List<Anuncis> getAll() {

		return getHibernateTemplate().loadAll(Anuncis.class);
	}
}
