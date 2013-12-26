package com.alexmany.appStore.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.alexmany.appStore.dao.UsersDao;
import com.alexmany.appStore.exceptions.DAOException;
import com.alexmany.appStore.model.UserRole;
import com.alexmany.appStore.model.Users;

public class UsersDaoImpl extends HibernateDaoSupport implements UsersDao{

	/**
	 * Mira si existeix l'usuari si. Si no existeix el guarda i retorna l'id
	 * @param Users
	 * @return id Users
	 * @throws DAOException si ja existia
	 */
	public Long save( Users user ){
		Long idUser = null;
		Users userInDB = findByUsername(user.getUsername());
		if(userInDB==null){		 
			idUser = (Long) getHibernateTemplate().save(user);
		}else{
			throw new DAOException("User exist");
		}
		return idUser;
	}

	
	public void update( Users user ){

		getHibernateTemplate().update(user);
	}

	public void delete( Users user ){

		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		Users userToDelete = (Users) session.load(Users.class, user.getId());
		//session.delete(userToDelete.getUserRole());
		session.delete(userToDelete);
		session.getTransaction().commit();
		session.close();

		// getHibernateTemplate().delete(user);
	}

	@SuppressWarnings("unchecked")
	public Users findByUsername( String username ){

		List<Users> userFounds = (List<Users>) getHibernateTemplate().find("from Users u where u.username = ?", username);
		if (userFounds.isEmpty())
			return null;
		Users userFound = userFounds.get(0);
		return userFound;
	}


	
	
	public Users load( Long id ){
		@SuppressWarnings("unchecked")
		List<Users> userFounds = (List<Users>) getHibernateTemplate().find("from Users u where u.id = ?",id);
		if (userFounds.isEmpty())
			return null;
		Users userFound = userFounds.get(0);
		return userFound;
		// getHibernateTemplate().load(Users.class,id);
	}
	
	@Transactional
	public List<Users> getAll(){

		return getHibernateTemplate().loadAll(Users.class);
	}
	 
	public void  saveRole(UserRole usuRole){
	
			getHibernateTemplate().save(usuRole);
	
	}
	
	public UserRole loadRole( Long id ){
		
		return getHibernateTemplate().load(UserRole.class,id);
	}
}
