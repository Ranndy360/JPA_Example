package com.jsf.jpa.crud.db.operations;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.jsf.jpa.crud.StudentBean;
import com.jsf.jpa.crud.StudentEntityManager;

public class DatabaseOperations {

	private static final String PERSISTENCE_UNIT_NAME = "JSF_JPA_Example";	
	private static EntityManager entityMgrObj;
	private static EntityTransaction transactionObj;
	
	
	public DatabaseOperations() {
		entityMgrObj = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
		transactionObj = entityMgrObj.getTransaction();
	}
	// Method To Fetch All Student Details From The Database
	@SuppressWarnings("unchecked")
	public static List getAllStudentDetails() {
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM StudentEntityManager s");
		List studentList = queryObj.getResultList();
		if (studentList != null && studentList.size() > 0) {			
			return studentList;
		} else {
			
			return null;
		}
	}

	// Method To Add Create Student Details In The Database
	public static String createNewStudent(String name,String password,String email) {
		if(!transactionObj.isActive()) {
			transactionObj.begin();
		}

		StudentEntityManager newStudentObj = new StudentEntityManager();
		newStudentObj.setId(getMaxStudentId());
		newStudentObj.setName(name);
		newStudentObj.setEmail(email);
		newStudentObj.setPassword(password);
		entityMgrObj.persist(newStudentObj);
		transactionObj.commit();
		return "studentsList.xhtml?faces-redirect=true";	
	}

	// Method To Delete The Selected Student Id From The Database 
	public static String deleteStudentDetails(int studentId) {
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}

		StudentEntityManager deleteStudentObj = new StudentEntityManager();
		if(isStudentIdPresent(studentId)) {
			deleteStudentObj.setId(studentId);
			entityMgrObj.remove(entityMgrObj.merge(deleteStudentObj));
		}		
		transactionObj.commit();
		return "studentsList.xhtml?faces-redirect=true";
	}

	// Method To Update The Student Details For A Particular Student Id In The Database
	public static String updateStudentDetails(int studentId, String updatedStudentName,String email, String password) {
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}

		if(isStudentIdPresent(studentId)) {
			Query queryObj = entityMgrObj.createQuery("UPDATE StudentEntityManager s SET s.student_name=:name,s.student_email=:email,s.student_password=:password WHERE s.student_id= :id");			
			queryObj.setParameter("id", studentId);
			queryObj.setParameter("name", updatedStudentName);
			queryObj.setParameter("email", email);
			queryObj.setParameter("password", password);
			int updateCount = queryObj.executeUpdate();
			if(updateCount > 0) {
				System.out.println("Record For Id: " + studentId + " Is Updated");
			}
		}
		transactionObj.commit();
		FacesContext.getCurrentInstance().addMessage("editStudentForm:studentId", new FacesMessage("Student Record #" + studentId + " Is Successfully Updated In Db"));
		return "studentEdit.xhtml";
	}

	// Helper Method 1 - Fetch Maximum Student Id From The Database
	private static int getMaxStudentId() {
		int maxStudentId = 1;
		Query queryObj = entityMgrObj.createQuery("SELECT MAX(s.student_id)+1 FROM StudentEntityManager s");
		if(queryObj.getSingleResult() != null) {
			maxStudentId = (Integer) queryObj.getSingleResult();
		}
		return maxStudentId;
	}

	// Helper Method 2 - Fetch Particular Student Details On The Basis Of School Id From The Database
	private static boolean isStudentIdPresent(int studentId) {
		boolean idResult = false;
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM StudentEntityManager s WHERE s.student_id = :id");
		queryObj.setParameter("id", studentId);
		StudentEntityManager selectedStudentId = (StudentEntityManager) queryObj.getSingleResult();
		if(selectedStudentId != null) {
			idResult = true;
		}
		return idResult;
	}
}
