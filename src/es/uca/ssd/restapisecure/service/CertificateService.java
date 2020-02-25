package es.uca.ssd.restapisecure.service;

import java.util.List;

import es.uca.ssd.restapisecure.dao.CertificateDao;
import es.uca.ssd.restapisecure.dao.CourseDao;
import es.uca.ssd.restapisecure.model.CertificateEntity;
import es.uca.ssd.restapisecure.model.CourseEntity;

public class CertificateService {
	
	private CertificateDao certificateDao;
	
	public CertificateService() {
		this.certificateDao = new CertificateDao();
	}
	
	public CertificateEntity getCertificate(int id) {
		return certificateDao.getCertificate(id);
	}

	public List<CertificateEntity> getCertificates() {
		return certificateDao.getCertificates();
	}
	
	public CertificateEntity findById(Integer id) {
		return certificateDao.findById(id);
	}

	public CertificateEntity create(CertificateEntity certificate) {
		return certificateDao.create(certificate);
	}

	public CertificateEntity create(String course, String annotation, Integer note,  Boolean pass) {
		return certificateDao.create(new CertificateEntity(course, annotation, note, pass));
	}
	
	public CertificateEntity update(CertificateEntity certificate) {
		return certificateDao.update(certificate);
	}

	public void delete(int id) {
		certificateDao.delete(id);
	}

}
