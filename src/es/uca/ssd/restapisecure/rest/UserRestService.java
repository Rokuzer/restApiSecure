package es.uca.ssd.restapisecure.rest;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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
	
	@GET
	@Path("/getusers")
	//@Produces(MediaType.APPLICATION_JSON)
	public List<UserEntity> getUsers(){
		UserService userService = new UserService();
		List<UserEntity> listUsers = userService.getUsers();
		return listUsers;
	}
	
	@POST
	@Path("/createwithkey")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserEntity getApiKey(UserEntity myUser) {
		UUID apikey = UUID.randomUUID();
		UserService userService = new UserService();
		UserEntity user = userService.create(myUser.getUsername(), myUser.getPassword(), myUser.getName(), myUser.getSurname(), myUser.getEmail());		
		user.setApiKey(apikey.toString());
		return user;
	}
	
	@POST
	@Path("/testApi")
	@Produces(MediaType.TEXT_PLAIN)
	public String testing(@HeaderParam("id") Integer id, @HeaderParam("apikey") String apikey) {
		UserService userService = new UserService();
		UserEntity User = userService.getUser(id);
		return User.getApiKey().toString();
	/*	if (User != null) {
			if (User.getApiKey().equals(apikey)) {
				return "GRANTED";
			}
		}
		return "Denied";*/
	}

}
