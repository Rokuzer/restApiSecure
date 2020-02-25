package es.uca.ssd.restapisecure.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.jose4j.jwk.JsonWebKey;

import es.uca.ssd.restapisecure.dao.CourseDao;
import es.uca.ssd.restapisecure.filter.JwtTokenRequired;
import es.uca.ssd.restapisecure.model.CourseEntity;
import es.uca.ssd.restapisecure.model.UserEntity;
import es.uca.ssd.restapisecure.service.CourseService;
import es.uca.ssd.restapisecure.service.UserService;


@Path("/courses")
public class CourseRestService {

	public static JsonWebKey myJwk = null;

	private static Validator validator;

	private CourseService courseService;
	
	private static Logger logger = Logger.getLogger(CourseRestService.class);
	
	public CourseRestService() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		
		courseService = new CourseService();
	}
	
	//GetCourses...
	//@JwtTokenRequired
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAllCourses() {
		List<CourseEntity> courses = courseService.getCourses();

		return Response.ok().entity(courses).build();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response lookupUserById(@PathParam("id") Integer id) {
		CourseEntity course = courseService.findById(id);
		if (course == null) {
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", "Course with ID=`" + id + "` not found");
			return Response.status(Response.Status.NOT_FOUND).entity(responseObj).build();
		}
		return Response.ok().entity(course).build();
	}
	
	//Delete course for ID.
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeUser(@PathParam("id") Integer id) {
		CourseEntity course = courseService.findById(id);
		if (course == null) {
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", "User with ID=`" + id + "` not found");
			return Response.status(Response.Status.NOT_FOUND).entity(responseObj).build();
		} else if (courseService.delete(id)) {
			return Response.ok().build();
		}
		throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
	}
	
	//-------------------------------------
	
	@GET
	@Path("existcourse/{name}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response searchCourse(@PathParam("name")String name) throws Exception {
		try {
			if (!checkInput(name.equals("%") ? "a" : name)) {
				logger.info("Intent of insert a invalid input"+Status.NOT_FOUND.getStatusCode());
				return Response.status(Response.Status.BAD_REQUEST).entity("invalid input").build();
			}
			if(courseService.existCourse(name))
				return Response.ok("The course searched, exist").header("X-Content-Type-Options", "nosniff").build();
			else
				return Response.ok("Sorry, the course not exist").header("X-Content-Type-Options", "nosniff").build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("updateNamecourse")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response updateCourse(@HeaderParam("name")String name,  @HeaderParam("idUser") String id, @HeaderParam("apikey") String apikey, @HeaderParam("idCourse")Integer idCourse) throws Exception{
	try {

		UserService userService = new UserService();

			UserEntity User = userService.findById(id);
			
			CourseDao courseDao = new CourseDao();
			
		if (User != null && User.getApiKey().equals(apikey)) {
			
					CourseEntity course = courseService.getCourse(idCourse);
					
					if(course!=null) {
						
						course.setName(name);
						courseDao.update(course);
						return Response.ok("the course has been update.").header("X-Content-Type-Options", "nosniff").build();
					}
					else {
						return Response.ok("The course not exist").header("X-Content-Type-Options", "nosniff").build();
					}
			}
			else {
				return Response.status(Response.Status.UNAUTHORIZED).build();	
			}
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	public boolean checkInput(String input) {	
		Pattern regexPattern = Pattern.compile("^[A-Za-z0-9-]+$");
		return regexPattern.matcher(input).find();
	}

}
