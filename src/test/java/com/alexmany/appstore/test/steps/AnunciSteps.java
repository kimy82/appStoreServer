package com.alexmany.appstore.test.steps;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import junit.framework.Assert;

import org.apache.wink.common.model.multipart.InMultiPart;
import org.apache.wink.common.model.multipart.InPart;
import org.apache.wink.server.utils.LinkBuilders;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.mockito.Mockito;

import com.alexmany.appStore.dao.AnuncisDao;
import com.alexmany.appStore.dao.UsersDao;
import com.alexmany.appStore.model.Anuncis;
import com.alexmany.appStore.model.Users;
import com.alexmany.appStore.restfull.UsersService;
import com.google.gson.Gson;

/**
 * 
 * @author kim
 * 
 */
public class AnunciSteps {

	private String responseString;
	private UsersService srv;
	private String userName;
	private UriInfo uriInfo;
	private LinkBuilders link;
	private UsersDao userDaoMock;
	private AnuncisDao anunciDaoMock;
	private Users userFound;
	private Integer numberOfAnuncis;
	private Integer init;

	@Given("I save a foto without presaved anunci")
	public void testSaveFotoWithOutAnunci() throws Exception {
		
		
		prepareServiceScenario1();
		
		InMultiPart inMultiPartMock = Mockito.mock(InMultiPart.class);
		Mockito.when(inMultiPartMock.hasNext()).thenReturn(true).thenReturn(false);
		InPart inPart =Mockito.mock(InPart.class);
		// Get current classloader
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream inputStream = cl.getResourceAsStream("imageTest.jpg");
		Mockito.when(inPart.getBody(InputStream.class, null)).thenReturn(inputStream);
		Mockito.when(inMultiPartMock.next()).thenReturn(inPart);
		this.responseString = this.srv.uploadFoto(inMultiPartMock, uriInfo);
		Mockito.verify(uriInfo, Mockito.times(2)).getQueryParameters();
		Mockito.verify(this.userDaoMock, Mockito.times(1)).load(new Long("1"));
		Mockito.verify(this.anunciDaoMock, Mockito.times(1)).load(new Long("1"));
		Assert.assertEquals(responseString, "{\"ok\":\"ok\",\"id\":\"1\"}");
		
	}

	@When("I get $response")
	public void testGetResponse(@Named("response") String response) {

		Assert.assertEquals(responseString, response);
	}

	@Then("If I create anunci has the same $id")
	public void testCheckIdAnunci(@Named("id") String id) {

		this.responseString = srv.saveAnunci(link, uriInfo);

		Mockito.verify(uriInfo, Mockito.times(6)).getQueryParameters();
		Mockito.verify(this.anunciDaoMock, Mockito.times(1)).save(Mockito.any(Anuncis.class));
		Mockito.verify(this.anunciDaoMock, Mockito.times(2)).update(Mockito.any(Anuncis.class));

	}
	
	
	@Given("I create anunci")
	public void testInsertRest() {

		prepareServiceScenario2();

		this.responseString = srv.saveAnunci(link, uriInfo);

		Mockito.verify(uriInfo, Mockito.times(4)).getQueryParameters();
		Mockito.verify(this.anunciDaoMock, Mockito.times(1)).save(Mockito.any(Anuncis.class));
	}

	
	@Then("If I save a foto has the same $id")
	public void testCheckResponseForLogin(@Named("id") String id) throws Exception {
		
		InMultiPart inMultiPartMock = Mockito.mock(InMultiPart.class);
		Mockito.when(inMultiPartMock.hasNext()).thenReturn(true).thenReturn(false);
		InPart inPart =Mockito.mock(InPart.class);
		// Get current classloader
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream inputStream = cl.getResourceAsStream("imageTest.jpg");
		Mockito.when(inPart.getBody(InputStream.class, null)).thenReturn(inputStream);
		Mockito.when(inMultiPartMock.next()).thenReturn(inPart);
		this.responseString = this.srv.uploadFoto(inMultiPartMock, uriInfo);
		Mockito.verify(uriInfo, Mockito.times(6)).getQueryParameters();
		Mockito.verify(this.userDaoMock, Mockito.times(1)).load(new Long("1"));
		Mockito.verify(this.anunciDaoMock, Mockito.times(1)).load(new Long("1"));
		Assert.assertEquals(responseString, "{\"ok\":\"ok\",\"id\":\"1\"}");

	}
	
	
	@Given("There are $x anuncis")
	public void thereareX(@Named("x") String x) {

		this.anunciDaoMock = Mockito.mock(AnuncisDao.class);
		
		this.numberOfAnuncis = Integer.parseInt(x);

	}

	@When("I the anuncis with $init")
	public void testGetAnuncis(@Named("init") String init) {

		List <Anuncis> anuncisList = new ArrayList<Anuncis>();
		this.init = Integer.parseInt(init);

		for(int i= this.init ; i <this.numberOfAnuncis; i++){
			if(i-this.init>=20)break;
			anuncisList.add(new Anuncis("titol"+i, "descripcio","preu"));
		}
		Mockito.when(anunciDaoMock.getAll(Integer.parseInt(init))).thenReturn(anuncisList);
		
		srv.setAnuncisDao(this.anunciDaoMock);
	}
	
	@Then("I get $y anuncis")
	public void testRecieveAnmuncis(@Named("y") String y) throws Exception {
		
		uriInfo = Mockito.mock(UriInfo.class);
		@SuppressWarnings("unchecked")
		MultivaluedMap<String, String> multi = Mockito
				.mock(MultivaluedMap.class);
		List<String> init = new ArrayList<String>();
		init.add(String.valueOf(this.init));
		Mockito.when(multi.get("init")).thenReturn(init);
		
		Mockito.when(uriInfo.getQueryParameters()).thenReturn(multi);
		link = Mockito.mock(LinkBuilders.class);		
		
		this.responseString = this.srv.getAnuncis(link, uriInfo);
		Mockito.verify(uriInfo, Mockito.times(1)).getQueryParameters();
		
		Mockito.verify(this.anunciDaoMock, Mockito.times(1)).getAll(this.init);
		List objects = new Gson().fromJson(this.responseString, List.class);
		Assert.assertEquals(Integer.parseInt(y), objects.size());	
	}

	
	/**
	 * Create de mocks for the AnunciDAO and the params to rest service
	 */
	private void prepareServiceScenario1() {

		srv = new UsersService();

		setBasicParameters1();
		
		// Mocks for hibernate
		this.anunciDaoMock = Mockito.mock(AnuncisDao.class);
		Mockito.when(anunciDaoMock.save(Mockito.any(Anuncis.class))).thenReturn(new Long("1"));
		Mockito.doNothing().when(anunciDaoMock).update(Mockito.any(Anuncis.class));
		Anuncis anunci = new Anuncis("titol","descripco","preu");
		anunci.setId(new Long("1"));
		Mockito.doReturn(anunci).when(anunciDaoMock).load(new Long("1"));
		
		this.userDaoMock = Mockito.mock(UsersDao.class);		
		Mockito.when(this.userDaoMock.load(new Long("1"))).thenReturn(Mockito.mock(Users.class));
		
		
		srv.setAnuncisDao(anunciDaoMock);
		srv.setUsersDao(userDaoMock);

	}
	
	
	
	private void setBasicParameters1(){
		// Mocks for restfull params
		uriInfo = Mockito.mock(UriInfo.class);
		@SuppressWarnings("unchecked")
		MultivaluedMap<String, String> multi = Mockito
				.mock(MultivaluedMap.class);
		List<String> titols = new ArrayList<String>();
		titols.add("titols");
		List<String> descripcio = new ArrayList<String>();
		descripcio.add("descripcio");
		List<String> preus = new ArrayList<String>();
		preus.add("preu");
		List<String> idUsers = new ArrayList<String>();
		idUsers.add("1");
		List<String> idAnuncis = new ArrayList<String>();
		idAnuncis.add("null");
		List<String> idAnuncisSegon = new ArrayList<String>();
		idAnuncisSegon.add("1");
		Mockito.when(multi.get("titol")).thenReturn(titols);
		Mockito.when(multi.get("descripcio")).thenReturn(descripcio);
		Mockito.when(multi.get("preu")).thenReturn(preus);
		Mockito.when(multi.get("idUser")).thenReturn(idUsers);
		Mockito.when(multi.get("idAnunci")).thenReturn(idAnuncis).thenReturn(idAnuncisSegon);
		Mockito.when(uriInfo.getQueryParameters()).thenReturn(multi);
		link = Mockito.mock(LinkBuilders.class);

	}
	/**
	 * Create de mocks for the AnunciDAO and the params to rest service
	 */
	private void prepareServiceScenario2() {

		srv = new UsersService();

		setBasicParameters2();
		
		// Mocks for hibernate
		this.anunciDaoMock = Mockito.mock(AnuncisDao.class);
		Mockito.when(anunciDaoMock.save(Mockito.any(Anuncis.class))).thenReturn(new Long("1"));
		Anuncis anunci = new Anuncis("titol","descripco","preu");
		anunci.setId(new Long("1"));
		Mockito.doReturn(anunci).when(anunciDaoMock).load(new Long("1"));
		
		this.userDaoMock = Mockito.mock(UsersDao.class);		
		Mockito.when(this.userDaoMock.load(new Long("1"))).thenReturn(Mockito.mock(Users.class));
		
		srv.setAnuncisDao(anunciDaoMock);
		srv.setUsersDao(userDaoMock);
		

	}
	
	
	
	private void setBasicParameters2(){
		// Mocks for restfull params
		uriInfo = Mockito.mock(UriInfo.class);
		@SuppressWarnings("unchecked")
		MultivaluedMap<String, String> multi = Mockito
				.mock(MultivaluedMap.class);
		List<String> titols = new ArrayList<String>();
		titols.add("titols");
		List<String> descripcio = new ArrayList<String>();
		descripcio.add("descripcio");
		List<String> preus = new ArrayList<String>();
		preus.add("preu");
		List<String> idUsers = new ArrayList<String>();
		idUsers.add("1");
		List<String> idAnuncis = new ArrayList<String>();
		idAnuncis.add("null");
		List<String> idAnuncisSegon = new ArrayList<String>();
		idAnuncisSegon.add("1");
		Mockito.when(multi.get("titol")).thenReturn(titols);
		Mockito.when(multi.get("descripcio")).thenReturn(descripcio);
		Mockito.when(multi.get("preu")).thenReturn(preus);
		Mockito.when(multi.get("idUser")).thenReturn(idUsers);
		Mockito.when(multi.get("idAnunci")).thenReturn(idAnuncis).thenReturn(idAnuncisSegon);
		Mockito.when(uriInfo.getQueryParameters()).thenReturn(multi);
		link = Mockito.mock(LinkBuilders.class);

	}

}