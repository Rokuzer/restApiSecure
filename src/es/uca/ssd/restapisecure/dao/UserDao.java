package es.uca.ssd.restapisecure.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.uca.ssd.restapisecure.exception.DuplicateEmailException;
import es.uca.ssd.restapisecure.exception.DuplicateUsernameException;
import es.uca.ssd.restapisecure.model.UserEntity;
import es.uca.ssd.restapisecure.util.HibernateUtil;

public class UserDao {

	public List<UserEntity> findAllOrderedByName() {
		Transaction transaction = null;
		List<UserEntity> listOfUser = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// get an user object
			listOfUser = session.createNamedQuery("User.allUsersOrderedByUsername", UserEntity.class).getResultList();

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
		return listOfUser;
	}

	public UserEntity findById(String id) {
		Transaction transaction = null;
		UserEntity user = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// get an user object
			user = session.get(UserEntity.class, id);

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
		return user;
	}

	public UserEntity findByUsername(String username) {
		Transaction transaction = null;
		UserEntity user = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// get an user object
			TypedQuery<UserEntity> query = session.createNamedQuery("User.findByUsername", UserEntity.class);
			query.setParameter("username", username);
			query.setMaxResults(1);
			user = query.getSingleResult();

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
		return user;
	}

	public UserEntity findByEmail(String email) {
		Transaction transaction = null;
		UserEntity user = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// get an user object
			TypedQuery<UserEntity> query = session.createNamedQuery("User.findByEmail", UserEntity.class);
			query.setParameter("email", email);
			query.setMaxResults(1);
			user = query.getSingleResult();

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
		return user;
	}

	public UserEntity create(UserEntity user) throws DuplicateUsernameException, DuplicateEmailException {
		checkDuplicateUsername(user.getUsername());
		checkDuplicateEmail(user.getEmail());

		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// save the student object
			session.persist(user);

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

	private void checkDuplicateUsername(String username) throws DuplicateUsernameException {
		if (this.findByUsername(username) != null) {
			throw new DuplicateUsernameException("Username " + username + " already exists");
		}
	}

	private void checkDuplicateEmail(String email) throws DuplicateEmailException {
		if (this.findByEmail(email) != null) {
			throw new DuplicateEmailException("Email " + email + " already exists");
		}
	}

	public UserEntity update(UserEntity user) throws DuplicateEmailException {
		checkDuplicateEmail(user);
		
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// save the student object
			session.update(user);
			session.flush();

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

	private void checkDuplicateEmail(UserEntity user) throws DuplicateEmailException {
		UserEntity storedUser = this.findByEmail(user.getEmail());
		if (storedUser != null && !storedUser.getId().equals(user.getId())) {
			throw new DuplicateEmailException("Email " + user.getEmail() + " already exists");
		}
	}

	public void delete(String id) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// Delete a user object
			UserEntity user = findById(id);
			if (user != null) {
				session.delete(user);
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

}
