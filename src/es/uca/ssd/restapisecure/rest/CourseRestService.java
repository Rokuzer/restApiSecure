package es.uca.ssd.restapisecure.rest;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import es.uca.ssd.restapisecure.dao.CourseDao;
import es.uca.ssd.restapisecure.model.CourseEntity;
import es.uca.ssd.restapisecure.model.UserEntity;
import es.uca.ssd.restapisecure.service.CourseService;
import es.uca.ssd.restapisecure.service.UserService;


@Path("/courses")
public class CourseRestService {
	
	private CourseService courseService;
	//habilitate the logs in class.
	private static Logger logger = Logger.getLogger(CourseRestService.class);
	
	public CourseRestService() {
		courseService = new CourseService();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getCourse() {
		CourseEntity course = courseService.create("name1", "description1", 16);
		return courseService.getCourse(course.getId()).toString();
	}
	
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
	
	@GET
	@Path("/getcourses")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourses() throws Exception {
		try {
			CourseService courseService = new CourseService();
			List<CourseEntity> listCourses = courseService.getCourses();
			return Response.ok(listCourses).header("X-Content-Type-Options", "nosniff").build();
		} catch(Exception e) {
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
	
	public boolean checkInput(String... input) {	
		Pattern regexPattern = Pattern.compile("^[A-Za-z0-9-]+$");
		
		for (String s: input) {
			if (!regexPattern.matcher(s).find()) {
				//logger.info("Warning: Input not allowed " + s);
		        return false;
			}
		}
		
		return true;
	}

}
