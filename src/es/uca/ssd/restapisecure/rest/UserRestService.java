package es.uca.ssd.restapisecure.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import es.uca.ssd.restapisecure.model.UserEntity;
import es.uca.ssd.restapisecure.service.UserService;

@Path("/users")
public class UserRestService {
	
	private UserService userService;
	
	public UserRestService() {
		userService = new UserService();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getUser() {
		UserEntity user = userService.create("username1", "password1", "name1", "surname1", "email1");
		return userService.getUser(user.getId()).toString();
	}

}
