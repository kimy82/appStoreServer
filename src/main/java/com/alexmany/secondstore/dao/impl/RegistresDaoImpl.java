package com.alexmany.secondstore.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.alexmany.secondstore.dao.RegistresDao;
import com.alexmany.secondstore.exceptions.DAOException;
import com.alexmany.secondstore.model.NotificationRegister;

public class RegistresDaoImpl extends HibernateDaoSupport implements RegistresDao {

	/**
	 * 
	 * @param register
	 * @return id long
	 */
	public Long save(NotificationRegister register) {
		Long idRegistre=null;
		try {
			if (register.getId() == null) {
				idRegistre = (Long) getHibernateTemplate().save(register);
			}
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		return idRegistre;
	}
	
	/**
	 * 
	 * @param register
	 * @return id long
	 */
	public void update(String registerold, String registernew) {
		NotificationRegister registerOld = new NotificationRegister(registerold);
		NotificationRegister registerOldFromDb = loadByRegisterId(registerOld);
		if(registerOldFromDb!=null){
			registerOldFromDb.setRegisterId(registernew);
			save(registerOldFromDb);
		}
	}

	/**
	 * Delete the register
	 */
	public void delete(NotificationRegister register) {

		NotificationRegister registerToDelete =loadByRegisterId(register);
		if(registerToDelete== null){
			return;
		}
		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(registerToDelete);
		session.getTransaction().commit();
		session.close();
	}
	
	/**
	 * load by registerId
	 */
	public NotificationRegister loadByRegisterId(NotificationRegister register) {
		
		NotificationRegister notificationRegister = null;
		@SuppressWarnings("unchecked")
		List<NotificationRegister> notificationRegisterList = (List<NotificationRegister>) getHibernateTemplate().find(
				"from NotificationRegister a where a.registerId = ?", register.getRegisterId());
		if (notificationRegisterList.isEmpty())
			return null;
		notificationRegister = notificationRegisterList.get(0);
		return notificationRegister;
	}
	
	/**
	 * Delete the register
	 */
	public List<NotificationRegister> loadAll() {

		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<NotificationRegister> notificationRegisterList = (List<NotificationRegister>) session.createQuery("from NotificationRegister a ").setCacheable(false).list();		
		session.close();
		return notificationRegisterList;
	}

}
