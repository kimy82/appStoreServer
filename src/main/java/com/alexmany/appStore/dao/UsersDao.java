package com.alexmany.appStore.dao;

import java.util.List;

import com.alexmany.appStore.model.UserRole;
import com.alexmany.appStore.model.Users;


public interface UsersDao {
	
	void save(Users user) ;
	
	void update(Users user);
	
	void delete(Users user);
	
	Users findByUsername(String username);
	
	List<Users> getAll();
	
	Users load( Long id );
	
	void saveRole(UserRole usuRole);
	
	UserRole loadRole( Long id );
	
}
