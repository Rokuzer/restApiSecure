package es.uca.ssd.restapisecure.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;

import es.uca.ssd.restapisecure.filter.JwtTokenOptional;
import es.uca.ssd.restapisecure.filter.JwtTokenRequired;
import es.uca.ssd.restapisecure.model.CertificateEntity;
import es.uca.ssd.restapisecure.service.CertificateService;

@Path("/certificates")
public class CertificateRestService {
	
	private static Logger logger = Logger.getLogger(CertificateRestService.class);
	
	private CertificateService certificateService;
	
	@Context
	private SecurityContext sctx;
	
	public CertificateRestService() {
		certificateService = new CertificateService();
	}
	
	@JwtTokenOptional
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAllUsers() {
		List<CertificateEntity> certificates = certificateService.getCertificates();
		
		logger.info("GET /certificates - userid=" + sctx.getUserPrincipal().getName() + " - OK");

		Map<String, List<CertificateEntity>> responseObj = new HashMap<>();
		responseObj.put("users", certificates);
		return Response.ok().entity(responseObj).build();
	}
	
	@JwtTokenOptional
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response lookupUserById(@PathParam("id") Integer id) {
		CertificateEntity certificate = certificateService.findById(id);
		if (certificate == null) {
			logger.error("GET /certificates/" + id + " - userid=" + sctx.getUserPrincipal().getName() + " - ERROR: Certificate with ID=`\" + id + \"` not found");
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", "Certificate with ID=`" + id + "` not found");
			return Response.status(Response.Status.NOT_FOUND).entity(responseObj).build();
		}
		logger.info("GET /certificates/" + id + " - userid=" + sctx.getUserPrincipal().getName() + " - OK");
		return Response.ok().entity(certificate).build();
	}
	
	@JwtTokenRequired
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCertificate(CertificateEntity certificate) {
		Response.ResponseBuilder builder = null;
		try {
			// Validates user using bean validation
			certificateService.create(certificate);

			logger.info("POST /certificates - userid=" + sctx.getUserPrincipal().getName() + " - OK");
			
			// Create an "ok" response
			builder = Response.ok().entity(certificate);
		} catch (Exception e) {
			// Handle generic exceptions
			logger.error("POST /certificates - userid=" + sctx.getUserPrincipal().getName() + " - ERROR: " + e.getMessage());
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}

		return builder.build();
	}

	

}
