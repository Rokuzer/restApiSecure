package es.uca.ssd.restapisecure.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.uca.ssd.restapisecure.model.CertificateEntity;
import es.uca.ssd.restapisecure.model.CourseEntity;
import es.uca.ssd.restapisecure.util.HibernateUtil;

public class CertificateDao {

	public CertificateEntity getCertificate(int id) {
		Transaction transaction = null;
		CertificateEntity certificate = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();
			// get an user object
			certificate = session.get(CertificateEntity.class, id);
			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return certificate;
	}

	@SuppressWarnings("unchecked")
	public List<CertificateEntity> getCertificates() {
		Transaction transaction = null;
		List<CertificateEntity> listOfCertificate = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// get an user object
			listOfCertificate = session.createQuery("from CertificateEntity").getResultList();

			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfCertificate;
	}

	public CertificateEntity create(CertificateEntity certificate) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// save the student object
			session.save(certificate);

			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return certificate;
	}

	public CertificateEntity update(CertificateEntity certificate) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// save the student object
			session.update(certificate);

			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return certificate;
	}

	public void delete(int id) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// Delete a user object
			CertificateEntity certificate = session.get(CertificateEntity.class, id);
			if (certificate != null) {
				session.delete(certificate);
			}

			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
	
	public CertificateEntity findById(Integer id) {
		Transaction transaction = null;
		CertificateEntity certificate = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// get an user object
			certificate = session.get(CertificateEntity.class, id);

			// commit transaction
			transaction.commit();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return certificate;
	}
	

}
