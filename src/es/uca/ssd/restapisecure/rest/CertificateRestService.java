package es.uca.ssd.restapisecure.rest;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uca.ssd.restapisecure.exception.DuplicateEmailException;
import es.uca.ssd.restapisecure.exception.DuplicateUsernameException;
import es.uca.ssd.restapisecure.model.CertificateEntity;
import es.uca.ssd.restapisecure.model.CourseEntity;
import es.uca.ssd.restapisecure.model.UserEntity;
import es.uca.ssd.restapisecure.service.CertificateService;

@Path("/certificates")
public class CertificateRestService {
	
	/*
	 * API courses:
	 * Generar certificado:
	 * POST /generatecertificate/{idcourse}/{iduser}
	 * 
	 * Obtener certificado:
	 * GET /getcertificate/{idcertificate}
	 * 
	 */
	
	private CertificateService certificateService;
	
	public CertificateRestService() {
		certificateService = new CertificateService();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getUser() {
		CertificateEntity certificate = certificateService.create("course1", "all ok", 8, true);
		return certificateService.getCertificate(certificate.getId()).toString();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response lookupUserById(@PathParam("id") Integer id) {
		CertificateEntity certificate = certificateService.findById(id);
		if (certificate == null) {
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", "Certificate with ID=`" + id + "` not found");
			return Response.status(Response.Status.NOT_FOUND).entity(responseObj).build();
		}
		return Response.ok().entity(certificate).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCertificate(CertificateEntity certificate) {
		Response.ResponseBuilder builder = null;
		try {
			// Validates user using bean validation
			//?validateCertificate(certificate);

			certificateService.create(certificate);
		
			// Create an "ok" response
			builder = Response.ok().entity(certificate);
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

	

}
