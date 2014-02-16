package com.alexmany.appStore.dao;

import java.util.List;

import com.alexmany.appStore.model.NotificationRegister;


public interface RegistresDao {
	
	Long save(NotificationRegister register) ;
	void delete(NotificationRegister register);
	NotificationRegister loadByRegisterId(NotificationRegister register);
	List<NotificationRegister> loadAll();
	void update(String registerold, String registernew);
	
}
