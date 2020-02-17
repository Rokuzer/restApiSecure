package es.uca.ssd.restapisecure.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.uca.ssd.restapisecure.model.UserEntity;
import es.uca.ssd.restapisecure.util.HibernateUtil;

public class UserDao {

	public UserEntity create(UserEntity user) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();
			// save the student object
			session.save(user);
			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return user;
	}

}
