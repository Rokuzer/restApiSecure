package es.uca.ssd.restapisecure.rest;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import es.uca.ssd.restapisecure.exception.DuplicateEmailException;
import es.uca.ssd.restapisecure.exception.DuplicateUsernameException;
import es.uca.ssd.restapisecure.filter.JwtTokenOptional;
import es.uca.ssd.restapisecure.filter.JwtTokenRequired;
import es.uca.ssd.restapisecure.model.UserEntity;
import es.uca.ssd.restapisecure.service.UserService;

@Path("/users")
public class UserRestService {

	public static JsonWebKey myJwk = null;

	private static Validator validator;

	private UserService userService;
	
	@Context
	private SecurityContext sctx;
	
	public UserRestService() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		userService = new UserService();
	}

	@JwtTokenRequired
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAllUsers() {
		List<UserEntity> users = userService.findAllOrderedByUsername();
		for (UserEntity user : users) {
			sanitizeUser(user);
		}

		Map<String, List<UserEntity>> responseObj = new HashMap<>();
		responseObj.put("users", users);
		return Response.ok().entity(responseObj).build();
	}

	@JwtTokenOptional
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response lookupUserById(@PathParam("id") String id) {
		UserEntity user = userService.findById(id);
		if (user == null) {
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", "User with ID=`" + id + "` not found");
			return Response.status(Response.Status.NOT_FOUND).entity(responseObj).build();
		}
		if (sctx.getUserPrincipal() == null || sctx.getUserPrincipal().getName() == null) {
			sanitizeUser(user);
		}
		return Response.ok().entity(user).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(UserEntity user) {
		Response.ResponseBuilder builder = null;
		try {
			// Validates user using bean validation
			validateUser(user);

			userService.create(user);
			sanitizeUser(user);

			// Create an "ok" response
			builder = Response.ok().entity(user);
		} catch (DuplicateUsernameException e) {
			// Handle the username duplication
			// TODO:
			// DuplicateUsernameException.class.getSimpleName(),
			// UserRestService.class.getSimpleName(), "Username " + user.getUsername() + "
			// already exists"
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("username", "Username already exists");
			builder = Response.status(Response.Status.PRECONDITION_FAILED).entity(responseObj);
		} catch (DuplicateEmailException e) {
			// Handle the email duplication
			// TODO:
			// DuplicateEmailException.class.getSimpleName(),
			// UserRestService.class.getSimpleName(), "Email " + user.getEmail() + "
			// already exists"
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("email", "Email already exists");
			builder = Response.status(Response.Status.PRECONDITION_FAILED).entity(responseObj);
		} catch (ConstraintViolationException e) {
			// Handle bean validation issues
			// TODO:
			// ConstraintViolationException.class.getSimpleName(),
			// UserRestService.class.getSimpleName(), "Catched exception: " +
			// e.getLocalizedMessage()
			builder = createViolationResponse(e.getConstraintViolations());
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			// TODO:
			// ValidationException.class.getSimpleName(),
			// UserRestService.class.getSimpleName(), "Catched exception: " +
			// e.getLocalizedMessage()
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("email", "Email taken");
			builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		} catch (Exception e) {
			// Handle generic exceptions
			// TODO:
			// Exception.class.getSimpleName(),
			// UserRestService.class.getSimpleName(), "Catched exception: " +
			// e.getMessage()
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}

		return builder.build();
	}

	private void validateUser(UserEntity user) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);

		if (!violations.isEmpty()) {
			// TODO:
			// ConstraintViolationException.class.getSimpleName(),
			// UserRestService.class.getSimpleName(), "Found " + violations.size() + "
			// violations."
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
		// TODO:
		// Exception.class.getSimpleName(),
		// UserRestService.class.getSimpleName(), "Validation completed. Found
		// violations: " + violations.size()

		Map<String, String> responseObj = new HashMap<>();

		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}

	private void sanitizeUser(UserEntity user) {
		user.setPassword(String.join("", Collections.nCopies(user.getPassword().length(), "*")));
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editUser(@PathParam("id") String id, UserEntity user) {
		Response.ResponseBuilder builder = null;
		try {
			// Validates user using bean validation
			validateUser(user);

			userService.update(id, user);

			// Create an "ok" response
			builder = Response.ok().entity(user);
		} catch (DuplicateEmailException e) {
			// Handle the email duplication
			// TODO:
			// DuplicateEmailException.class.getSimpleName(),
			// UserRestService.class.getSimpleName(), "Email " + user.getEmail() + "
			// already exists"
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("email", "Email already exists");
			builder = Response.status(Response.Status.PRECONDITION_FAILED).entity(responseObj);
		} catch (ConstraintViolationException e) {
			// Handle bean validation issues
			// TODO:
			// ConstraintViolationException.class.getSimpleName(),
			// UserRestService.class.getSimpleName(), "Catched exception: " +
			// e.getLocalizedMessage()
			builder = createViolationResponse(e.getConstraintViolations());
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			// TODO:
			// ValidationException.class.getSimpleName(),
			// UserRestService.class.getSimpleName(), "Catched exception: " +
			// e.getLocalizedMessage()
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("email", "Email taken");
			builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		} catch (Exception e) {
			// Handle generic exceptions
			// TODO:
			// Exception.class.getSimpleName(),
			// UserRestService.class.getSimpleName(), "Catched exception: " +
			// e.getMessage()
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}

		return builder.build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeUser(@PathParam("id") String id) {
		UserEntity user = userService.findById(id);
		if (user == null) {
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", "User with ID=`" + id + "` not found");
			return Response.status(Response.Status.NOT_FOUND).entity(responseObj).build();
		} else if (userService.delete(id)) {
			return Response.ok().build();
		}
		throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
	}

	@POST
	@Path("/generateApiKey")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApiKey(UserEntity user) {
		if (user == null) {
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", "User was not provided");
			return Response.status(Response.Status.PRECONDITION_FAILED).entity(responseObj).build();
		} else if (userService.findById(user.getId()) == null) {
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", "User with ID=`" + user.getId() + "` not found");
			return Response.status(Response.Status.NOT_FOUND).entity(responseObj).build();
		} else {
			String apiKey = userService.generateApiKey(user.getId());
			if (apiKey != null && !apiKey.isEmpty()) {
				Map<String, String> responseObj = new HashMap<>();
				responseObj.put("apikey", apiKey);
				return Response.ok().entity(responseObj).build();
			}
		}

		Map<String, String> responseObj = new HashMap<>();
		responseObj.put("error", "Unmanaged error");
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj).build();
	}

	@POST
	@Path("/testApiKey")
	@Produces(MediaType.TEXT_PLAIN)
	public String testing(@HeaderParam("id") String id, @HeaderParam("apikey") String apiKey) {
		UserEntity user = userService.findById(id);
		if (user.getApiKey() != null && apiKey != null && user.getApiKey().equals(apiKey)) {
			return "Access granted";
		}
		return "Access denied";

	}

	@GET
	@Path("/authenticateJWT")
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticateCredentials(@HeaderParam("username") String username,
			@HeaderParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		if (username == null || password == null) {
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", "Please, configure headers properly");
			return Response.status(Response.Status.PRECONDITION_FAILED).entity(responseObj).build();
		}
		UserEntity storedUser = userService.findByUsername(username);
		if (storedUser == null) {
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", "User with username=`" + username + "` not found");
			return Response.status(Response.Status.NOT_FOUND).entity(responseObj).build();
		}

		UserEntity user = new UserEntity();
		user.setId(storedUser.getId());
		user.setUsername(username);
		user.setPassword(password);

		RsaJsonWebKey jwk = null;
		try {
			jwk = RsaJwkGenerator.generateJwk(2048);
			jwk.setKeyId("1");
			myJwk = jwk;
		} catch (JoseException e) {
			e.printStackTrace();
		}

		String jwtToken = userService.generateJwtToken(user, jwk);
		Map<String, String> responseObj = new HashMap<>();
		responseObj.put("token", jwtToken);
		return Response.ok().entity(responseObj).build();
	}

	@POST
	@Path("/testJWT")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response testJWT(@HeaderParam("token") String token, String myName)
			throws JsonGenerationException, JsonMappingException, IOException {
		JsonWebKey jwk = myJwk;
		// Validate Token's authenticity and check claims
		JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime().setAllowedClockSkewInSeconds(30)
				.setRequireSubject().setExpectedIssuer("uca").setVerificationKey(jwk.getKey()).build();
		try {
			// Validate the JWT and process it to the Claims
			JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
			System.out.println("JWT validation succeeded! " + jwtClaims);
		} catch (InvalidJwtException e) {
			return Response.status(Response.Status.FORBIDDEN.getStatusCode()).entity("Forbidden").build();
		}
		String sayHello = "Hello " + myName;
		return Response.status(200).entity(sayHello).build();
	}

}
