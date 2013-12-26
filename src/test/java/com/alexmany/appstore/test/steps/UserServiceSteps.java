package com.alexmany.appstore.test.steps;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import junit.framework.Assert;

import org.apache.wink.server.utils.LinkBuilders;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import com.alexmany.appStore.dao.UsersDao;
import com.alexmany.appStore.model.UserRole;
import com.alexmany.appStore.model.Users;
import com.alexmany.appStore.restfull.UsersService;

/**
 * 
 * @author kim
 * 
 */
public class UserServiceSteps {

	private String responseString;
	private UsersService srv;
	private String userName;
	private UriInfo uriInfo;
	private LinkBuilders link;
	private UsersDao userDaoMock;
	private Users userFound;

	@Given("I delete a $user")
	public void testSentRest(@Named("user") String user) {

		this.userName = user;

		prepareServiceScenario1();

		srv.setUsersDao(userDaoMock);

		this.responseString = srv.delete(link, uriInfo).toString();
 
		Mockito.verify(uriInfo, Mockito.times(1)).getQueryParameters();
		Mockito.verify(userDaoMock, Mockito.times(1)).findByUsername(
				Mockito.anyString());
		if (!this.userName.equals("xxxx") && !this.userName.equals("null")) {
			Mockito.verify(userDaoMock).delete(Mockito.eq(userFound));
		}
		
	}

	@When("I recieve $response")
	public void testGetResponse(@Named("response") String response) {

		Assert.assertEquals(responseString, response);
	}

	@Then("It do not exist in BBDD and the response is $responselogin")
	public void testCheckResponse(@Named("responselogin") String responselogin) {

		this.responseString = this.srv.login(link, uriInfo);
		Assert.assertEquals(responseString, responselogin);

	}
	
	
	@Given("I insert a $user")
	public void testInsertRest(@Named("user") String user) {

		this.userName = user;

		prepareServiceScenario2();

		srv.setUsersDao(userDaoMock);

		this.responseString = srv.insert(link, uriInfo);

		Mockito.verify(uriInfo, Mockito.times(2)).getQueryParameters();
		Mockito.verify(userDaoMock, Mockito.times(1)).findByUsername(
				Mockito.anyString());
		if (!this.userName.equals("xxxx") && !this.userName.equals("null")) {
			Mockito.verify(userDaoMock).save(Mockito.any(Users.class));
		}
	}

	
	@Then("I can login with response $responselogin")
	public void testCheckResponseForLogin(@Named("responselogin") String responselogin) {

		this.responseString = this.srv.login(link, uriInfo);
		Assert.assertEquals(responseString, responselogin);

	}
	/**
	 * Create de mocks for the userDao and the params to rest service
	 */
	private void prepareServiceScenario1() {

		srv = new UsersService();

		setBasicParameters();
		
		// Mocks for hibernate
		userDaoMock = Mockito.mock(UsersDao.class);

		if (this.userName.equals("xxxx") || this.userName.equals("null")) {

			userFound = Mockito.mock(Users.class);
			userFound.setUsername(userName);
			Mockito.when(userDaoMock.findByUsername(userName)).thenReturn(null);
			Mockito.doThrow(Exception.class).when(userDaoMock)
					.delete(userFound);

		} else {
			userFound = Mockito.mock(Users.class);
			userFound.setUsername(userName);
			Mockito.when(userDaoMock.findByUsername(userName))
					.thenReturn(userFound).thenReturn(null);
			Mockito.doNothing().when(userDaoMock).delete(userFound);

		}

	}
	
	/**
	 * Create de mocks for the userDao and the params to rest service
	 */
	private void prepareServiceScenario2() {

		srv = new UsersService();

		setBasicParameters();
		
		// Mocks for hibernate
		userDaoMock = Mockito.mock(UsersDao.class);

		if (this.userName.equals("xxxx") || this.userName.equals("null")) {

			userFound = new Users();
			userFound.setUsername(userName);
			userFound.setPassword("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8");
			Mockito.when(userDaoMock.findByUsername(userName)).thenReturn(userFound);

		} else {
			userFound = new Users();
			userFound.setUsername(userName);
			userFound.setPassword("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8");
			Mockito.when(userDaoMock.findByUsername(userName))
					.thenReturn(null).thenReturn(userFound);
			Mockito.doReturn(new Long("1")).when(userDaoMock).save(Mockito.any(Users.class));
			UserRole userRole = new UserRole();
			userRole.setRole("CLIENT_ROLE");
			Mockito.doReturn(userRole).when(userDaoMock).loadRole(Mockito.anyLong());
			Mockito.doNothing().when(userDaoMock).saveRole(Mockito.any(UserRole.class));
		}

	}
	
	private void setBasicParameters(){
		// Mocks for restfull params
		uriInfo = Mockito.mock(UriInfo.class);
		@SuppressWarnings("unchecked")
		MultivaluedMap<String, String> multi = Mockito
				.mock(MultivaluedMap.class);
		List<String> users = new ArrayList<String>();
		users.add(userName);
		List<String> passwords = new ArrayList<String>();
		users.add(userName);
		passwords.add("password");
		Mockito.when(multi.get("user")).thenReturn(users);
		Mockito.when(multi.get("pass")).thenReturn(passwords);
		Mockito.when(uriInfo.getQueryParameters()).thenReturn(multi);
		link = Mockito.mock(LinkBuilders.class);

	}

}