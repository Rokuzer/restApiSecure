package es.uca.ssd.restapisecure.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import es.uca.ssd.restapisecure.rest.UserRestService;

@Provider
@JwtTokenRequired
@Priority(Priorities.AUTHENTICATION)
public class JwtTokenRequiredFilter implements ContainerRequestFilter {
	
	private static String HTTP_HEADER_TOKEN = "token";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Get the HTTP Authorization header from the request
		String token = requestContext.getHeaderString(HTTP_HEADER_TOKEN);
		JsonWebKey jwk = UserRestService.myJwk;
		
		List<String> userinfo = new ArrayList<>();
		userinfo.add("uno");
		userinfo.add("dos");
//		requestContext.getHeaders().put("userinfo", new ArrayList<>());

		if (token == null || jwk == null) {
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", "invalid token " + token);
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(responseObj).build());
		} else {
			// Validate Token's authenticity and check claims
			JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime()
					.setAllowedClockSkewInSeconds(30).setRequireSubject().setExpectedIssuer("uca")
					.setVerificationKey(jwk.getKey()).build();
			try {
				// Validate the JWT and process it to the Claims
				JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
				// logger.info("JWT validation succeeded! " + jwtClaims);
			} catch (InvalidJwtException e) {
				Map<String, String> responseObj = new HashMap<>();
				responseObj.put("error", "invalid token " + token);
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(responseObj).build());
			}
		}
	}

}
