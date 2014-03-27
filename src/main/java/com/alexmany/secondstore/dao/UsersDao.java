package com.alexmany.secondstore.dao;

import java.util.List;

import com.alexmany.secondstore.model.UserRole;
import com.alexmany.secondstore.model.Users;


public interface UsersDao {
	
	Long save(Users user) ;
	
	void update(Users user);
	
	void delete(Users user);
	
	Users findByUsername(String username);
	
	List<Users> getAll();
	
	Users load( Long id );
	
	void saveRole(UserRole usuRole);
	
	UserRole loadRole( Long id );
	
	Users findByUserEmail( String email );
	
	int getNumProductsSold( Long id );
	
	int getNumProducts( Long id );
	
	Users loadWtihChats( Long id );
	
}
