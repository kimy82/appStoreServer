/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 *******************************************************************************/

package com.alexmany.appStore.restfull;

import java.security.NoSuchAlgorithmException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.wink.common.annotations.Workspace;
import org.apache.wink.server.utils.LinkBuilders;
import org.hibernate.HibernateException;

import com.alexmany.appStore.dao.UsersDao;
import com.alexmany.appStore.model.UserRole;
import com.alexmany.appStore.model.Users;
import com.alexmany.appStore.utils.Constants;
import com.alexmany.appStore.utils.Utils;

@Workspace(workspaceTitle = "services", collectionTitle = "userService")
@Path("/service/userService")
public class UsersService {

	UsersDao usersDao;

	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/delete")
	public String delete(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {

		try {
	
			String userName = uriInfo.getQueryParameters().get("user").get(0);

			
			Users userFound = this.usersDao.findByUsername(userName);
			if (userFound == null){
				return "{\"ok\":\"ko\"}";
			}
				 
			this.usersDao.delete(userFound);

			 return "{\"ok\":\"ok\"}";
			
			
		} catch (Exception e) {
			return "{\"ok\":\"ko\"}";
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/insert")
	public String insert(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {

		try {

			String userName = uriInfo.getQueryParameters().get("user").get(0);
			String password = uriInfo.getQueryParameters().get("pass").get(0);
			

			Users userFound = this.usersDao.findByUsername(userName);
			if (userFound != null)
				return "{\"ok\":\"ko\"}";

			Users user = new Users();
			user.setPassword(password);
			user.setUsername(userName);

			UserRole userRole = new UserRole();

			userRole = usersDao.loadRole(Constants.ROLE_CLIENT);

			user.setUserRole(userRole);
			this.usersDao.save(user);

			return "{\"ok\":\"ok\"}";

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/login")
	public String login(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {

		try {

			String json = "";
			String userName = uriInfo.getQueryParameters().get("user").get(0);
			String password = uriInfo.getQueryParameters().get("pass").get(0);
			if (userName == null || userName.equals("") || password == null
					|| password.equals("") || userName.equals("null")
					|| password.equals("null")) {
				return "{\"ok\":\"ko\"}";
			}

			Users user = this.usersDao.findByUsername(userName);

			if (user == null) {
				return "{\"ok\":\"ko\"}";
			} else {

				if (!user.getPassword().equals(Utils.createSHA(password))) {
					return "{\"ok\":\"ko\"}";
				}
				String role = user.getUserRole() == null ? "ROLE_CLIENT" : user
						.getUserRole().getRole();

				json = "{\"ok\":\"ok\",\"username\":\"" + userName
						+ "\",\"role\":\"" + role + "\"}";
			}

			return json;
		} catch (HibernateException e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";
		}
	}

	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

}
