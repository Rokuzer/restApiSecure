package es.uca.ssd.restapisecure.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import es.uca.ssd.restapisecure.model.CertificateEntity;
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

}
