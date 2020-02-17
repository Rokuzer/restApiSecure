package es.uca.ssd.restapisecure.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import es.uca.ssd.restapisecure.dao.UserDao;
import es.uca.ssd.restapisecure.model.UserEntity;

@Path("/users")
public class UserRestService {
	
	private UserDao userDao;
	
	public UserRestService() {
		userDao = new UserDao();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getUser() {
		return userDao.create(new UserEntity("username1", "password1", "name1", "surname1", "email1")).getUsername();
	}

}
