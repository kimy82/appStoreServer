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
import com.alexmany.secondstore.exceptions.RestException;
import com.alexmany.secondstore.model.Anuncis;
import com.alexmany.secondstore.model.ImageAnunci;
import com.alexmany.secondstore.model.UserRole;
import com.alexmany.secondstore.model.Users;
import com.alexmany.secondstore.pojos.AnuncisTO;
import com.alexmany.secondstore.pojos.AnuncisTOSearch;
import com.alexmany.secondstore.utils.Constants;
import com.alexmany.secondstore.utils.ImageUtils;
import com.alexmany.secondstore.utils.UserUtils;
import com.alexmany.secondstore.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

			String email =  Utils.getInfoFromUriInfo(uriInfo, Constants.EMAIL_KEY, String.class);

			Users userFound = this.usersDao.findByUserEmail(email);
			if (userFound == null) {
				throw new RestException("delete :: USER NOT FOUND we can not delete it");
			}

			this.usersDao.delete(userFound);

			return "{\"ok\":\"ok\"}";

		} catch (RestException e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/insert")
	public String insert(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {

		try {

			String userName = Utils.getInfoFromUriInfo(uriInfo, Constants.USERNAME_KEY, String.class);
			String email = Utils.getInfoFromUriInfo(uriInfo, Constants.EMAIL_KEY, String.class);
			String password = Utils.getInfoFromUriInfo(uriInfo, Constants.PASSWORD_KEY, String.class);
			Float longitud = Utils.getInfoFromUriInfo(uriInfo, Constants.LONGITUD_KEY, Float.class);
			Float latitud = Utils.getInfoFromUriInfo(uriInfo,Constants.LATITUD_KEY, Float.class);

			Users userFound = this.usersDao.findByUserEmail(email);

			// Vol dir que ve del facebook
			if (userFound != null) {
				if (password.equals("facebook"))
					return "{\"ok\":\"ok\",\"id\":\"" + userFound.getId()
							+ "\"}";
				else
					throw new RestException("insert :: USER FOUND not coming from facebook");
			}

			Users user = new Users();
			user.setPassword(password);
			user.setUsername(userName);
			user.setEmail(email);
			try {
				user.setLatitud(latitud);
				user.setLongitud(longitud);
			} catch (NumberFormatException e) {
				user.setLatitud(null);
				user.setLongitud(null);
			}
			UserRole userRole = new UserRole();

			userRole = usersDao.loadRole(Constants.ROLE_CLIENT);

			user.setUserRole(userRole);
			Long id = this.usersDao.save(user);

			return "{\"ok\":\"ok\",\"id\":\"" + id + "\"}";
			
		} catch (RestException e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";
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
			
			String email = Utils.getInfoFromUriInfo(uriInfo, Constants.EMAIL_KEY, String.class);
			String password =  Utils.getInfoFromUriInfo(uriInfo, Constants.PASSWORD_KEY, String.class);
			
			if (email == null || email.equals("") || password == null
					|| password.equals("") || email.equals("null")
					|| password.equals("null")) {
				throw new RestException("login ::Compulsory fields for login are null or empty");
			}

			Users user = this.usersDao.findByUserEmail(email);

			if (user == null) {
				throw new RestException("login :: User does not exist");
			} else {

				if (!user.getPassword().equals(Utils.createSHA(password))) {
					throw new RestException("login :: User password does not match");
				}
				String role = user.getUserRole() == null ? "ROLE_CLIENT" : user
						.getUserRole().getRole();

				json = "{\"ok\":\"ok\",\"username\":\"" + user.getUsername()
						+ "\",\"role\":\"" + role + "\",\"id\":\""
						+ user.getId() + "\"}";
			}

			return json;
		
		}catch(RestException re){
			re.printStackTrace();
			return "{\"ok\":\"ko\"}";
		} catch (HibernateException e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";
		}catch (Exception e) {
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

			Long idAnunciFromClient = Utils.getInfoFromUriInfo(uriInfo, Constants.IDANUNCI_KEY, Long.class);
			Long idUser = Utils.getInfoFromUriInfo(uriInfo, Constants.USERID_KEY, Long.class);

			if (idUser==null) {
				throw new RestException("uploadFoto :: ID user is null: It is not possible to Upload a foto");
			} else {
				user = this.usersDao.load(idUser);
			}

			if (idAnunciFromClient== null) {
				List<Anuncis> anuncisList = this.anuncisDao.searchBy("update",
						user.getId());
				if (anuncisList != null && !anuncisList.isEmpty()) {
					Anuncis anunciToBeUpdated = anuncisList.get(0);
					idAnunciFromClient = anunciToBeUpdated
							.getId();
					System.out.println("upload foto recored anunci.....");
				} else {
					anunci = new Anuncis("update", "update", "update");
					anunci.setUser(user);
					anunci.setDataCreacio(new Date());
					anunci.setEstat(Constants.ESTAT_ANUNCI_NEW);
					Long idAnunci = this.anuncisDao.save(anunci);
					if (idAnunci == null)
						throw new RestException("uploadFoto :: ID of anunci is NULL");
					idAnunciFromClient =idAnunci;
					System.out.println("upload foto new anunci.....");
				}

			}
			anunci = this.anuncisDao.load(new Long(idAnunciFromClient));

			while (inMP.hasNext()) {
				ImageUtils.path = Constants.pathToImages;
				InPart part = inMP.next();
				InputStream imageInputStream = part.getBody(InputStream.class,
						null);
				BufferedImage bufferedImage = ImageIO.read(imageInputStream);

				bufferedImage = ImageUtils.resizeImage(bufferedImage,
						ImageUtils.IMAGE_JPEG, 400, 400);
				String name = "img_" + idAnunciFromClient + "_" + idUser + "_"
						+ anunci.getImagesAnunci().size();
				ImageUtils
						.saveImage(bufferedImage, name, ImageUtils.IMAGE_JPEG);
				ImageAnunci imageAnunci = new ImageAnunci(name);
				imageAnunci.setAnunci(anunci);
				anunci.getImagesAnunci().add(imageAnunci);
				this.anuncisDao.update(anunci);

			}
			System.out.println("upload foto return......................"
					+ idAnunciFromClient);
			return "{\"ok\":\"ok\",\"id\":\"" + idAnunciFromClient + "\"}";
			
		} catch (RestException e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";
		} catch (Exception e) {
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

			String descripcio = Utils.getInfoFromUriInfo(uriInfo, Constants.DESCRIPCIO_KEY, String.class);
			String titol = Utils.getInfoFromUriInfo(uriInfo, Constants.TITOL_KEY, String.class);
			String preu =  Utils.getInfoFromUriInfo(uriInfo, Constants.PREU_KEY, String.class);
			Long idAnunci =  Utils.getInfoFromUriInfo(uriInfo, Constants.IDANUNCI_KEY, Long.class);
			Float longitud = Utils.getInfoFromUriInfo(uriInfo, Constants.LONGITUD_KEY, Float.class);
			Float latitud =  Utils.getInfoFromUriInfo(uriInfo, Constants.LATITUD_KEY, Float.class);
			Long iduser =  Utils.getInfoFromUriInfo(uriInfo, Constants.USERID_KEY, Long.class);
			String city =  Utils.getInfoFromUriInfo(uriInfo, Constants.CITY_KEY, String.class);

			Users user = this.usersDao.load(new Long(iduser));

			if (descripcio == null || descripcio.equals("") || titol == null
					|| titol.equals("") || preu == null || preu.equals("null")) {
				throw new RestException("saveAnunci ::Descripcio, titol or Preu are null");
			}

			List<Anuncis> anuncisList = this.anuncisDao.searchBy("update",user.getId());
			
			if (idAnunci==null) {
					Anuncis anunci = new Anuncis(titol, descripcio, preu);
					if(anuncisList !=null && !anuncisList.isEmpty()){
						//netejem possibles anuncis erronis
						for(Anuncis anuncitodelete : anuncisList){
							this.anuncisDao.delete(anuncitodelete);
						}
					}
					
				
					anunci.setEstat(Constants.ESTAT_ANUNCI_NEW);
					anunci.setDataCreacio(new Date());
					anunci.setUser(user);

					if (longitud == null ) {
						anunci.setLongitud(user.getLongitud());
					} else {
						anunci.setLongitud(longitud);
					}

					if (latitud == null ) {
						anunci.setLatitud(user.getLatitud());
					} else {
						anunci.setLatitud(user.getLatitud());
					}
					anunci.setCity(city);
					this.anuncisDao.save(anunci);
					idAnunci = anunci.getId();
				
			} else {
				Anuncis anunci = this.anuncisDao.load(idAnunci);
				System.out.println("save anunci id from app.....");
				anunci.setEstat(Constants.ESTAT_ANUNCI_NEW);
				anunci.setDataCreacio(new Date());
				anunci.setDescripcio(descripcio);
				anunci.setTitol(titol);
				anunci.setPreu(preu);
				anunci.setUser(user);
				anunci.setCity(city);
				if (longitud == null) {
					anunci.setLongitud(user.getLongitud());
				} else {
					anunci.setLongitud(longitud);
				}
				if (latitud == null) {
					anunci.setLatitud(user.getLatitud());
				} else {
					anunci.setLatitud(user.getLatitud());
				}
				this.anuncisDao.update(anunci);
			}
			return "{\"ok\":\"ok\",\"id\":\"" + idAnunci + "\"}";
		
		} catch (RestException e) {
			e.printStackTrace();
			return "{\"ok\":\"ko\"}";
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
		
		String init = Utils.getInfoFromUriInfo(uriInfo, Constants.INIT_LIST_KEY, String.class);
		Double lat = Utils.getInfoFromUriInfo(uriInfo, Constants.LATITUD_KEY, Double.class);
		Double lon = Utils.getInfoFromUriInfo(uriInfo, Constants.LONGITUD_KEY, Double.class);


		try {

			List<Anuncis> anuncisList = this.anuncisDao.getAll(Integer
					.parseInt(init));

			for (Anuncis anunci : anuncisList) {
				AnuncisTOSearch anunciToSearch = new AnuncisTOSearch(
						anunci.getId(), anunci.getPreu(), anunci.getEstat(),
						0.0, anunci.getTitol(), "", anunci.getDescripcio(),
						anunci.getCity());

				anunciToSearch = UserUtils.setImagePath(anunci, anunciToSearch, Constants.server);
				
				if (lat == null || lon == null					
						|| anunci.getLatitud() == null
						|| anunci.getLongitud() == null) {
					anunciToSearch.setDistance(0.0);
				} else {
					anunciToSearch.setDistance(6371 * Math.acos(Math.cos(Math
							.toRadians(lat))
							* Math.cos(Math.toRadians(anunci.getLatitud()))
							* Math.cos(Math.toRadians(anunci.getLongitud())
									- Math.toRadians(lon))
							+ Math.sin(Math.toRadians(lat))
							* Math.sin(Math.toRadians(anunci.getLatitud()))));

				}
				anuncisTOList.add(anunciToSearch);
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
					.excludeFieldsWithoutExposeAnnotation().create();
			String json = gson.toJson(anuncisTOList);
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
	 * @return json of anuncis. {anunci[path(url imatge),preu,titol]}
	 */
	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/getListUserAnuncis")
	public String getListUserAnuncis(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {

		List<AnuncisTOSearch> anuncisTOList = new ArrayList<AnuncisTOSearch>();
		Long userId = Utils.getInfoFromUriInfo(uriInfo, Constants.USERID_KEY, Long.class);
		
		try {

			List<Anuncis> anuncisList = this.anuncisDao.searchBy(userId);

			for (Anuncis anunci : anuncisList) {
				AnuncisTOSearch anunciToSearch = new AnuncisTOSearch(
						anunci.getId(), anunci.getPreu(), anunci.getEstat(),
						0.0, anunci.getTitol(), "", anunci.getDescripcio(),
						anunci.getCity());

				anunciToSearch = UserUtils.setImagePath(anunci, anunciToSearch, Constants.server);

				anuncisTOList.add(anunciToSearch);
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
					.excludeFieldsWithoutExposeAnnotation().create();
			String json = gson.toJson(anuncisTOList);
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
	 * @return json of anuncis within specific coords. {anunci[path(url
	 *         imatge),preu,titol]}
	 */
	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	@Path("/searchAnuncis")
	public String searchAnuncis(@Context LinkBuilders linkProcessor,
			@Context UriInfo uriInfo) {

		List<AnuncisTOSearch> anuncisTOList = new ArrayList<AnuncisTOSearch>();
		Float lon = Utils.getInfoFromUriInfo(uriInfo, Constants.LONGITUD_KEY, Float.class);
		Float lat = Utils.getInfoFromUriInfo(uriInfo, Constants.LATITUD_KEY, Float.class);
		Integer distance = Utils.getInfoFromUriInfo(uriInfo, Constants.DISTANCE_KEY, Integer.class);
		Integer init =  Utils.getInfoFromUriInfo(uriInfo, Constants.INIT_LIST_KEY, Integer.class);
		
		try {

			List<Anuncis> anuncisList = this.anuncisDao.search(
					init, distance,
					lat, lon);

			for (Anuncis anunci : anuncisList) {
				AnuncisTOSearch anunciToSearch = new AnuncisTOSearch(
						anunci.getId(), anunci.getPreu(), anunci.getEstat(),
						0.0, anunci.getTitol(), "", anunci.getDescripcio(),
						anunci.getCity());

				if (anunci.getImagesAnunci() != null
						&& !anunci.getImagesAnunci().isEmpty()) {
					anunciToSearch.setName("http://" + Constants.server
							+ "/AppStore/images/"
							+ anunci.getImagesAnunci().get(0).getName()
							+ ".jpg");
				}

				anunciToSearch.setDistance(6371 * Math.acos(Math.cos(Math
						.toRadians(56.467056))
						* Math.cos(Math.toRadians(anunci.getLatitud()))
						* Math.cos(Math.toRadians(anunci.getLongitud())
								- Math.toRadians(-2.976094))
						+ Math.sin(Math.toRadians(56.467056))
						* Math.sin(Math.toRadians(anunci.getLatitud()))));
				anuncisTOList.add(anunciToSearch);
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
					.excludeFieldsWithoutExposeAnnotation().create();
			String json = gson.toJson(anuncisTOList);
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
		Long idAnunci =  Utils.getInfoFromUriInfo(uriInfo, Constants.IDANUNCI_KEY, Long.class);

		try {

			if (idAnunci == null ) {
				throw new RestException("getInfoAnunci :: id of ANUNCI is NULL or Empty");
			}

			Anuncis anunci = this.anuncisDao.load(idAnunci);
			

			anuncisTO = new AnuncisTO(anunci.getLatitud(),
					anunci.getLongitud(), anunci.getId(), anunci.getPreu(),
					anunci.getEstat(), 0.0, anunci.getTitol(), "",
					anunci.getDescripcio(), anunci.getCity(),anunci.getUser().getUsername(), 
					this.usersDao.getNumProducts(anunci.getUser().getId()),
					this.usersDao.getNumProductsSold(anunci.getUser().getId()),anunci.getUser().getId() );

			
			anuncisTO = UserUtils.setImagePath(anunci, anuncisTO, Constants.server);
			
			anuncisTO.setDistance(0.0);
			anuncisTO.setUserId(anunci.getUser().getId());
			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
					.excludeFieldsWithoutExposeAnnotation().create();
			String json = gson.toJson(anuncisTO);
			return json;
			
		}catch(RestException re){
			re.printStackTrace();
			return "{\"ok\":\"ko\"}";
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
