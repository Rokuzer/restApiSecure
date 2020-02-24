package es.uca.ssd.restapisecure.service;

import java.util.List;
import java.util.UUID;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import es.uca.ssd.restapisecure.dao.UserDao;
import es.uca.ssd.restapisecure.exception.DuplicateEmailException;
import es.uca.ssd.restapisecure.exception.DuplicateUsernameException;
import es.uca.ssd.restapisecure.model.UserEntity;

public class UserService {

	private UserDao userDao;

	public UserService() {
		this.userDao = new UserDao();
	}

	public List<UserEntity> findAllOrderedByUsername() {
		return userDao.findAllOrderedByName();
	}

	public UserEntity findById(String id) {
		return userDao.findById(id);
	}

	public UserEntity findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	public UserEntity create(UserEntity user) throws DuplicateUsernameException, DuplicateEmailException {
		return userDao.create(user);
	}

	public UserEntity create(String username, String password, String name, String surname, String email)
			throws DuplicateUsernameException, DuplicateEmailException {
		return userDao.create(new UserEntity(username, password, name, surname, email));
	}

	public UserEntity update(String id, UserEntity user) throws DuplicateEmailException {
		UserEntity editUser = findById(id);
		if (user.getEmail() != null) {
			editUser.setEmail(user.getEmail());
		}
		if (user.getName() != null) {
			editUser.setName(user.getName());
		}
		if (user.getSurname() != null) {
			editUser.setSurname(user.getSurname());
		}

		return userDao.update(user);
	}

	public boolean delete(String id) {
		userDao.delete(id);
		return userDao.findById(id) == null;
	}

	public String generateApiKey(String id) {
		UUID apiKey = UUID.randomUUID();
		UserEntity user = findById(id);
		user.setApiKey(apiKey.toString());
		try {
			userDao.update(user);
		} catch (DuplicateEmailException e) {
			// This will never happen
		}
		return user.getApiKey();
	}

	public String generateJwtToken(UserEntity user, RsaJsonWebKey jwk) {
		JwtClaims claims = new JwtClaims();
		claims.setIssuer("uca");
		claims.setExpirationTimeMinutesInTheFuture(10);
		claims.setGeneratedJwtId();
		claims.setIssuedAtToNow();
		claims.setNotBeforeMinutesInThePast(2);
		claims.setSubject(user.getUsername());
		claims.setStringListClaim("roles", "basicRestUser");

		JsonWebSignature jws = new JsonWebSignature();
		jws.setPayload(claims.toJson());
		jws.setKeyIdHeaderValue(jwk.getKeyId());
		jws.setKey(jwk.getPrivateKey());
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

		String jwt = null;
		try {
			jwt = jws.getCompactSerialization();
		} catch (JoseException e) {
			System.out.println(e);
		}
		
		UserEntity storedUser = findByUsername(user.getUsername());
		storedUser.setApiKey(jwt);
		try {
			userDao.update(storedUser);
		} catch (DuplicateEmailException e) {
			// This will never happen
		}
		
		return storedUser.getApiKey();
	}

}
