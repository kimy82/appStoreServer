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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.wink.common.annotations.Workspace;
import org.apache.wink.common.internal.utils.MediaTypeUtils;
import org.apache.wink.common.model.multipart.InMultiPart;
import org.apache.wink.common.model.multipart.InPart;
import org.apache.wink.server.utils.LinkBuilders;
import org.hibernate.HibernateException;

import com.alexmany.appStore.dao.AnuncisDao;
import com.alexmany.appStore.dao.UsersDao;
import com.alexmany.appStore.model.Anuncis;
import com.alexmany.appStore.model.UserRole;
import com.alexmany.appStore.model.Users;
import com.alexmany.appStore.utils.Constants;
import com.alexmany.appStore.utils.ImageUtils;
import com.alexmany.appStore.utils.Utils;

@Workspace(workspaceTitle = "services", collectionTitle = "userService")
@Path("/service/userService")
public class UsersService {

	UsersDao usersDao;
	AnuncisDao anuncisDao;

	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/delete")
	public String delete(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {

		try {

			String userName = uriInfo.getQueryParameters().get("user").get(0);

			Users userFound = this.usersDao.findByUsername(userName);
			if (userFound == null) {
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

	@POST
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Consumes(MediaTypeUtils.MULTIPART_FORM_DATA)
	@Path("/uploadFoto")
	public String uploadFoto(InMultiPart inMP, @Context UriInfo uriInfo)
			throws IOException {

		try{
		String idUser = uriInfo.getQueryParameters().get("idUser").get(0);
		String idAnunciFromClient = uriInfo.getQueryParameters().get("idAnunci").get(0);
		
		//id null vol dir que encara no ha estat creat
		if(idAnunciFromClient.equals("null")){
			Long idAnunci =this.anuncisDao.save(new Anuncis("titol","descripcio","preu"));	
			if(idAnunci==null)
				throw new Exception();
			idAnunciFromClient = String.valueOf(idAnunci);
		}
		
		while (inMP.hasNext()) {
			
			InPart part = inMP.next();
			InputStream imageInputStream = part.getBody(InputStream.class, null);
			BufferedImage bufferedImage =ImageIO.read(imageInputStream);
			
			bufferedImage = ImageUtils.resizeImage(bufferedImage, ImageUtils.IMAGE_JPEG , 100, 100);
			
			ImageUtils.saveImage(bufferedImage, "img_"+idAnunciFromClient, ImageUtils.IMAGE_JPEG);
		}
		return "{\"ok\":\"ok\",\"id\":\"1\"}";
		}catch(Exception e){
			return "{\"ok\":\"ko\"}";
		}
	}
	
	/**
	 * 
	 * @param linkProcessor
	 * @param uriInfo
	 * @return json ok ok id id si ha anat be o ok ko si ha anat malament
	 */
	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/saveAnunci")
	public String saveAnunci(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {

		try {
			
			String descripcio = uriInfo.getQueryParameters().get("descripcio").get(0);
			String titol = uriInfo.getQueryParameters().get("titol").get(0);
			String preu = uriInfo.getQueryParameters().get("preu").get(0);
			String idAnunci = uriInfo.getQueryParameters().get("idAnunci").get(0);
			if (descripcio == null || descripcio.equals("") || titol == null
					|| titol.equals("") || preu ==null ||preu.equals("null")) {
				return "{\"ok\":\"ko\"}";
			}
			
			Anuncis anunci = new Anuncis(titol,descripcio,preu);
			if(idAnunci.equals("null")){
				Long idAnunciLong = this.anuncisDao.save(anunci);
				idAnunci = String.valueOf(idAnunciLong);
			}else{
				this.anuncisDao.update(anunci);
			}
			return "{\"ok\":\"ok\",\"id\":\""+idAnunci+"\"}";
		} catch (HibernateException e) {
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

	public void setAnuncisDao(AnuncisDao anuncisDao) {
		this.anuncisDao = anuncisDao;
	}

}
