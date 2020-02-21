package es.uca.ssd.restapisecure.rest;

import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uca.ssd.restapisecure.model.CourseEntity;
import es.uca.ssd.restapisecure.service.CourseService;

@Path("/courses")
public class CourseRestService {
	
	private CourseService courseService;
	
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
		if (!checkInput(name.equals("%") ? "a" : name))
	        return Response.status(Response.Status.BAD_REQUEST).entity("invalid input").build();
		
		if(courseService.existCourse(name))
			return Response.ok("The course searched, exist").build();
		else
			return Response.ok("Sorry, the course not exist").build();
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
