package es.uca.ssd.restapisecure.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.uca.ssd.restapisecure.model.CourseEntity;
import es.uca.ssd.restapisecure.util.HibernateUtil;

public class CourseDao {

	public CourseEntity getCourse(int id) {
		Transaction transaction = null;
		CourseEntity course = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();
			// get an user object
			course = session.get(CourseEntity.class, id);
			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return course;
	}

	@SuppressWarnings("unchecked")
	public List<CourseEntity> getCourses() {
		Transaction transaction = null;
		List<CourseEntity> listOfCourse = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// get an user object
			listOfCourse = session.createQuery("from CourseEntity").getResultList();

			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfCourse;
	}

	public CourseEntity create(CourseEntity course) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// save the student object
			session.save(course);

			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return course;
	}

	public CourseEntity update(CourseEntity course) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// save the student object
			session.update(course);

			// commit transaction
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return course;
	}
	
	public Boolean searchByName(String name) {
		Transaction transaction = null;
		Object listOfCourse = null;
		Boolean exist = false;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();
			// get an user object
			//CORREGIR
			listOfCourse = session.createQuery("from CourseEntity WHERE name LIKE :name").setParameter("name", name).getSingleResult();
			// commit transaction
			transaction.commit();
			if(listOfCourse != null)
				exist= true;
			
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return exist;
		
	}

	public void delete(int id) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// start a transaction
			transaction = session.beginTransaction();

			// Delete a user object
			CourseEntity course = session.get(CourseEntity.class, id);
			if (course != null) {
				session.delete(course);
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
