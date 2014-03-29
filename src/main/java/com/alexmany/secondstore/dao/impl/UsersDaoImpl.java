package com.alexmany.secondstore.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.alexmany.secondstore.dao.UsersDao;
import com.alexmany.secondstore.exceptions.DAOException;
import com.alexmany.secondstore.model.Anuncis;
import com.alexmany.secondstore.model.UserRole;
import com.alexmany.secondstore.model.Users;

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
		session.delete(userToDelete);
		session.getTransaction().commit();
		session.close();

	}

	@SuppressWarnings("unchecked")
	public Users findByUsername( String username ){

		List<Users> userFounds = (List<Users>) getHibernateTemplate().find("from Users u where u.username = ?", username);
		if (userFounds.isEmpty())
			return null;
		Users userFound = userFounds.get(0);
		return userFound;
	}
	
	@SuppressWarnings("unchecked")
	public Users findByUserEmail( String email ){

		List<Users> userFounds = (List<Users>) getHibernateTemplate().find("from Users u where u.email = ?", email);
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
	
	public Users loadWtihChats( Long id ){
		
		
		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		Users user = (Users) session.load(Users.class, id);
		Hibernate.initialize(user.getChats());
		for(int i=0; i<user.getChats().size(); i++){
			Hibernate.initialize(user.getChats().get(i).getUsers());
		}
		session.getTransaction().commit();
		session.close();
		return user;
	}
	
	
	
	/**
	 * 
	 * @param id of user
	 * @return number of products return 0 if id is null
	 */
	public int getNumProducts( Long id ){
		if (id == null)
			return 0;
		
		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		int numProducts = session.createCriteria(Anuncis.class).createAlias("user", "user").add(Restrictions.eq("user.id",id)).list().size();
		session.close();		
		
		return numProducts;
	}
	
	/**
	 * 
	 * @param id of user
	 * @return number of products solds return 0 if id is null
	 */
	public int getNumProductsSold( Long id ){
		if (id == null)
			return 0;
		
		Session session = this.getSessionFactory().openSession();
		session.beginTransaction();
		int numProducts = session.createCriteria(Anuncis.class).createAlias("user", "user").add(Restrictions.eq("user.id",id)).add(Restrictions.eq("sold", true)).list().size();
		session.close();
		
		return numProducts;
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
