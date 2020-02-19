package es.uca.ssd.restapisecure.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	public String getUser() {
		CourseEntity course = courseService.create("name1", "description1", 16);
		return courseService.getCourse(course.getId()).toString();
	}

}
