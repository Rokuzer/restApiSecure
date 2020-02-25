package es.uca.ssd.restapisecure.service;

import java.util.List;


import es.uca.ssd.restapisecure.dao.CourseDao;
import es.uca.ssd.restapisecure.model.CourseEntity;

public class CourseService {
	
	private CourseDao courseDao;
	
	public CourseService() {
		this.courseDao = new CourseDao();
	}
	
	public CourseEntity getCourse(int id) {
		return courseDao.getCourse(id);
	}

	public List<CourseEntity> getCourses() {
		return courseDao.getCourses();
	}
	
	public CourseEntity findById(Integer id) {
		return courseDao.findById(id);
	}

	public Boolean existCourse(String name) {
		return courseDao.searchByName(name);
	}
	

	public CourseEntity create(CourseEntity course) {
		return courseDao.create(course);
	}

	public CourseEntity create(String name, String description, Integer time) {
		return courseDao.create(new CourseEntity(name, description,time));
	}
	
	public CourseEntity update(CourseEntity course) {
		return courseDao.update(course);
	}

	public boolean delete(Integer id) {
		courseDao.delete(id);
		return courseDao.findById(id) == null;
	}

}
