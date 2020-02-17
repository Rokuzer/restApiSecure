package es.uca.ssd.restapisecure.service;

import java.util.List;

import es.uca.ssd.restapisecure.dao.UserDao;
import es.uca.ssd.restapisecure.model.UserEntity;

public class UserService {
	
	private UserDao userDao;
	
	public UserService() {
		this.userDao = new UserDao();
	}
	
	public UserEntity getUser(int id) {
		return userDao.getUser(id);
	}

	public List<UserEntity> getUsers() {
		return userDao.getUsers();
	}

	public UserEntity create(UserEntity user) {
		return userDao.create(user);
	}

	public UserEntity create(String username, String password, String name, String surname, String email) {
		return userDao.create(new UserEntity(username, password, name, surname, email));
	}
	
	public UserEntity update(UserEntity user) {
		return userDao.update(user);
	}

	public void delete(int id) {
		userDao.delete(id);
	}

}
