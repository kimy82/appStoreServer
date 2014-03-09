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

package com.alexmany.secondstore.restfull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.alexmany.secondstore.dao.AnuncisDao;
import com.alexmany.secondstore.dao.UsersDao;
import com.alexmany.secondstore.model.Anuncis;
import com.alexmany.secondstore.model.ImageAnunci;
import com.alexmany.secondstore.model.UserRole;
import com.alexmany.secondstore.model.Users;
import com.alexmany.secondstore.pojos.AnuncisTO;
import com.alexmany.secondstore.pojos.AnuncisTOSearch;
import com.alexmany.secondstore.utils.Constants;
import com.alexmany.secondstore.utils.ImageUtils;
import com.alexmany.secondstore.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Workspace(workspaceTitle = "services", collectionTitle = "userService")
@Path("/service/userService")
public class UsersService {

	UsersDao usersDao;
	AnuncisDao anuncisDao;
	
	//public final String server="www.alexmanydev.com";
	public final String server="localhost:8080";
	//public final String pathToImages="/var/www/vhosts/alexmanydev.com/appservers/apache-tomcat-7x/webapps/AppStore/images/";
	public final String pathToImages="C:\\apache-tomcat-7.0.47\\webapps\\AppStore\\images\\";
	


	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/delete")
	public String delete(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {

		try {

			String email = uriInfo.getQueryParameters().get("email").get(0);

			Users userFound = this.usersDao.findByUserEmail(email);
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
			String email = uriInfo.getQueryParameters().get("email").get(0);
			String password = uriInfo.getQueryParameters().get("pass").get(0);
			String longitud = uriInfo.getQueryParameters().get("lon").get(0);
			String latitud = uriInfo.getQueryParameters().get("lat").get(0);

			Users userFound = this.usersDao.findByUserEmail(email);
			
			//Vol dir que ve del facebook
			if (userFound != null){
				if(password.equals("facebook"))
					return "{\"ok\":\"ok\",\"id\":\""+userFound.getId()+"\"}";
				else
					throw new Exception();
			}
			
			Users user = new Users();
			user.setPassword(password);
			user.setUsername(userName);
			user.setEmail(email);
			try{
				user.setLatitud(new Float(latitud));
				user.setLongitud(new Float(longitud));
			}catch(NumberFormatException e){
				user.setLatitud(null);
				user.setLongitud(null);
			}
			UserRole userRole = new UserRole();

			userRole = usersDao.loadRole(Constants.ROLE_CLIENT);

			user.setUserRole(userRole);
			Long id = this.usersDao.save(user);

			return "{\"ok\":\"ok\",\"id\":\""+id+"\"}";

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
			String email = uriInfo.getQueryParameters().get("email").get(0);
			String password = uriInfo.getQueryParameters().get("pass").get(0);
			if (email == null || email.equals("") || password == null
					|| password.equals("") || email.equals("null")
					|| password.equals("null")) {
				return "{\"ok\":\"ko\"}";
			}

			Users user = this.usersDao.findByUserEmail(email);

			if (user == null) {
				return "{\"ok\":\"ko\"}";
			} else {

				if (!user.getPassword().equals(Utils.createSHA(password))) {
					return "{\"ok\":\"ko\"}";
				}
				String role = user.getUserRole() == null ? "ROLE_CLIENT" : user
						.getUserRole().getRole();

				json = "{\"ok\":\"ok\",\"username\":\"" + user.getUsername()
						+ "\",\"role\":\"" + role + "\",\"id\":\""+user.getId()+"\"}";
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
		Users user = null;
		Anuncis anunci = null;
		try {

			String idAnunciFromClient = uriInfo.getQueryParameters().get("idAnunci").get(0);
			String idUser = uriInfo.getQueryParameters().get("idUser").get(0);

		if(idUser.equals("null") || idUser.equals("") || idUser.equals("undefined")){
			throw new Exception();
		}else{
			user = this.usersDao.load(new Long(idUser));
		}
		
		if(idAnunciFromClient.equals("null") || idAnunciFromClient.equals("")|| idAnunciFromClient.equals("undefined")){
			List<Anuncis> anuncisList = this.anuncisDao.searchBy("update",user.getId());
			if(anuncisList!=null && !anuncisList.isEmpty()){
				Anuncis anunciToBeUpdated = anuncisList.get(0);
				idAnunciFromClient = String.valueOf(anunciToBeUpdated.getId());
				System.out.println("upload foto recored anunci.....");
			}else{
				anunci = new Anuncis("update","update","update");
				anunci.setUser(user);
				anunci.setDataCreacio(new Date());
				anunci.setEstat(Constants.ESTAT_ANUNCI_NEW);
				Long idAnunci =this.anuncisDao.save(anunci);	
				if(idAnunci==null)
					throw new Exception();
				idAnunciFromClient = String.valueOf(idAnunci);
				System.out.println("upload foto new anunci.....");
			}
		
		}
		anunci = this.anuncisDao.load(new Long(idAnunciFromClient));
		
		while (inMP.hasNext()) {
			ImageUtils.path=pathToImages;
			InPart part = inMP.next();
			InputStream imageInputStream = part.getBody(InputStream.class, null);
			BufferedImage bufferedImage =ImageIO.read(imageInputStream);
			
			bufferedImage = ImageUtils.resizeImage(bufferedImage, ImageUtils.IMAGE_JPEG , 400, 400);
			String name ="img_"+idAnunciFromClient+"_"+idUser+"_"+anunci.getImagesAnunci().size();
			ImageUtils.saveImage(bufferedImage, name, ImageUtils.IMAGE_JPEG);
			ImageAnunci imageAnunci = new ImageAnunci(name);
			imageAnunci.setAnunci(anunci);
			anunci.getImagesAnunci().add(imageAnunci);
			this.anuncisDao.update(anunci);
		
		}
		System.out.println("upload foto return......................"+idAnunciFromClient);
		return "{\"ok\":\"ok\",\"id\":\""+idAnunciFromClient+"\"}";
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
			String longitud = uriInfo.getQueryParameters().get("lon").get(0);
			String latitud = uriInfo.getQueryParameters().get("lat").get(0);
			String iduser = uriInfo.getQueryParameters().get("iduser").get(0);
			String city = uriInfo.getQueryParameters().get("city").get(0);
			
			Users user = this.usersDao.load(new Long(iduser));
			
			if (descripcio == null || descripcio.equals("") || titol == null
					|| titol.equals("") || preu ==null ||preu.equals("null")) {
				return "{\"ok\":\"ko\"}";
			}
			
			Anuncis anunci = new Anuncis(titol,descripcio,preu);
			if(idAnunci.equals("null") || idAnunci == null || idAnunci.equals("undefined")){
				Anuncis anunciToBeUpdated = null;
				List<Anuncis> anuncisList = this.anuncisDao.searchBy("update",user.getId());
				if(anuncisList!=null && !anuncisList.isEmpty()){
					System.out.println("save anunci recored anunci.....");
					anunciToBeUpdated = anuncisList.get(0);
					anunci.setId(anunciToBeUpdated.getId());
					anunci.setEstat(Constants.ESTAT_ANUNCI_NEW);
					anunci.setDataCreacio(new Date());
					anunci.setUser(user);
					
					if(longitud==null ||longitud.equals("undefined") || longitud.equals("")){
						anunci.setLongitud(user.getLongitud());
					}else{
						anunci.setLongitud(new Float(longitud));
					}
					
					if(latitud==null ||latitud.equals("undefined") || latitud.equals("")){
						anunci.setLatitud(user.getLatitud());
					}else{
						anunci.setLatitud(user.getLatitud());
					}
					anunci.setCity(city);
					this.anuncisDao.update(anunci);
					idAnunci = String.valueOf(anunci.getId());
				}else{
					System.out.println("save anunci new anunci.....");
					anunci.setEstat(Constants.ESTAT_ANUNCI_NEW);
					anunci.setDataCreacio(new Date());
					anunci.setUser(user);
					if(longitud==null ||longitud.equals("undefined") || longitud.equals("")){
						anunci.setLongitud(user.getLongitud());
					}else{
						anunci.setLongitud(new Float(longitud));
					}
					
					if(latitud==null ||latitud.equals("undefined") || latitud.equals("")){
						anunci.setLatitud(user.getLatitud());
					}else{
						anunci.setLatitud(user.getLatitud());
					}
					anunci.setCity(city);
					Long idAnunciLong = this.anuncisDao.save(anunci);
					idAnunci = String.valueOf(idAnunciLong);
				}
			}else{
				System.out.println("save anunci id from app.....");
				anunci.setEstat(Constants.ESTAT_ANUNCI_NEW);
				anunci.setDataCreacio(new Date());
				anunci.setUser(user);
				anunci.setCity(city);
				if(longitud==null ||longitud.equals("undefined") || longitud.equals("")){
					anunci.setLongitud(user.getLongitud());
				}else{
					anunci.setLongitud(new Float(longitud));
				}
				
				if(latitud==null ||latitud.equals("undefined") || latitud.equals("")){
					anunci.setLatitud(user.getLatitud());
				}else{
					anunci.setLatitud(user.getLatitud());
				}				
				anunci.setId(new Long(idAnunci));
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
	
	/**
	 * 
	 * @param linkProcessor
	 * @param uriInfo
	 * @return json of anuncis. {anunci[path(url imatge),preu,titol]}
	 */
	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/getAnuncis")
	public String getAnuncis(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {
		
		List<AnuncisTOSearch> anuncisTOList = new ArrayList<AnuncisTOSearch>();
		String init = uriInfo.getQueryParameters().get("init").get(0);
		String lat = uriInfo.getQueryParameters().get("lat").get(0);
		String lon = uriInfo.getQueryParameters().get("lon").get(0);
		
		try {
			
			List<Anuncis> anuncisList = this.anuncisDao.getAll(Integer.parseInt(init));
			
			for(Anuncis anunci : anuncisList){
				AnuncisTOSearch anunciToSearch = new AnuncisTOSearch(anunci.getId(), anunci.getPreu(), anunci.getEstat(), 0.0, anunci.getTitol(), "",anunci.getDescripcio(), anunci.getCity());
				
				if(anunci.getImagesAnunci()!=null && !anunci.getImagesAnunci().isEmpty()){
					anunciToSearch.setName("http://"+server+"/AppStore/images/"+anunci.getImagesAnunci().get(0).getName()+".jpg");
				}
				
				if(lat ==null || lat.equals("") || lon == null || lon.equals("")|| lon.equals("undefined")|| lat.equals("undefined") ||anunci.getLatitud()==null || anunci.getLongitud()==null){
					anunciToSearch.setDistance(0.0);
				}else{
					anunciToSearch.setDistance(6371*Math.acos(Math.cos(Math.toRadians(Double.parseDouble(lat)))*
							Math.cos(Math.toRadians(anunci.getLatitud()))*Math.cos(Math.toRadians(anunci.getLongitud())-Math.toRadians(Double.parseDouble(lon)))+Math.sin(Math.toRadians(Double.parseDouble(lat)))*Math.sin(Math.toRadians(anunci.getLatitud()))));
					
				}
				anuncisTOList.add(anunciToSearch);
			}
			
			 Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
			 String json= gson.toJson(anuncisTOList);
			 return json;
			
		} catch (HibernateException e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";		
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";
		}
	}
	
	/**
	 * 
	 * @param linkProcessor
	 * @param uriInfo
	 * @return json of anuncis within specific coords. {anunci[path(url imatge),preu,titol]}
	 */
	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/searchAnuncis")
	public String searchAnuncis(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {
		
		List<AnuncisTOSearch> anuncisTOList = new ArrayList<AnuncisTOSearch>();
		String lon = uriInfo.getQueryParameters().get("lon").get(0);
		String lat = uriInfo.getQueryParameters().get("lat").get(0);
		String distance = uriInfo.getQueryParameters().get("distance").get(0);
		String init = uriInfo.getQueryParameters().get("init").get(0);
		try {
			
			List<Anuncis> anuncisList = this.anuncisDao.search(Integer.parseInt(init),Integer.parseInt(distance),new Float(lat),new Float(lon));
			
			for(Anuncis anunci : anuncisList){
				AnuncisTOSearch anunciToSearch = new AnuncisTOSearch(anunci.getId(), anunci.getPreu(), anunci.getEstat(), 0.0, anunci.getTitol(), "",anunci.getDescripcio(), anunci.getCity());
				
				if(anunci.getImagesAnunci()!=null && !anunci.getImagesAnunci().isEmpty()){
					anunciToSearch.setName("http://"+server+"/AppStore/images/"+anunci.getImagesAnunci().get(0).getName()+".jpg");
				}
			
				anunciToSearch.setDistance(6371*Math.acos(Math.cos(Math.toRadians(56.467056))*
						Math.cos(Math.toRadians(anunci.getLatitud()))*Math.cos(Math.toRadians(anunci.getLongitud())-Math.toRadians(-2.976094))+Math.sin(Math.toRadians(56.467056))*Math.sin(Math.toRadians(anunci.getLatitud()))));
				anuncisTOList.add(anunciToSearch);
			}
			
			 Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
			 String json= gson.toJson(anuncisTOList);
			 return json;
			
		} catch (HibernateException e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";		
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";
		}
	}
	
	/**
	 * 
	 * @param linkProcessor
	 * @param uriInfo
	 * @return json of anunci.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/getInfoAnunci")
	public String getInfoAnunci(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {
		
		AnuncisTO anuncisTO = null;
		String idAnunci = uriInfo.getQueryParameters().get("id").get(0);
		
		try {
			
			if(idAnunci==null || idAnunci.equals("")){
				return "{\"ok\":\"ko\"}";
			}
			
			Anuncis anunci = this.anuncisDao.load(Long.parseLong(idAnunci));
			
			anuncisTO = new AnuncisTO(anunci.getLatitud(),anunci.getLongitud(),anunci.getId(), anunci.getPreu(), anunci.getEstat(), 0.0, anunci.getTitol(), "",anunci.getDescripcio(), anunci.getCity());
				
			List<String> images = new ArrayList<String>();
			if(anunci.getImagesAnunci()!=null && !anunci.getImagesAnunci().isEmpty()){
				images.add("http://"+server+"/AppStore/images/"+anunci.getImagesAnunci().get(0).getName()+".jpg");
			}
			anuncisTO.setImages(images);
			anuncisTO.setDistance(0.0);

			 Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
			 String json= gson.toJson(anuncisTO);
			 return json;
			
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