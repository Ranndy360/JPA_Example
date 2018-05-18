package com.jsf.jpa.crud;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.jsf.jpa.crud.db.operations.DatabaseOperations;

@ManagedBean
public class StudentBean {
	private int id;
	private String name;	
	private String editStudentId;
	private String email;
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEditStudentId() {
		return editStudentId;
	}

	public void setEditStudentId(String editStudentId) {
		this.editStudentId = editStudentId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// Method To Fetch The Existing School List From The Database
	public List studentListFromDb() {
		return DatabaseOperations.getAllStudentDetails();		
	}

	// Method To Add New School To The Database
	public String addNewStudent(StudentBean studentBean) {
		return DatabaseOperations.createNewStudent(studentBean.getName(),studentBean.getPassword(),studentBean.getEmail());		
	}

	// Method To Delete The School Details From The Database
	public String deleteStudentById(int studentId) {		
		return DatabaseOperations.deleteStudentDetails(studentId);		
	}

	// Method To Navigate User To The Edit Details Page And Passing Selecting School Id Variable As A Hidden Value
	public String editStudentDetailsById() {
		editStudentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedStudentId");		
		return "studentEdit.xhtml";
	}

	// Method To Update The School Details In The Database
	public String updateStudentDetails(StudentBean studentBean) {
		return DatabaseOperations.updateStudentDetails(Integer.parseInt(studentBean.getEditStudentId()), studentBean.getName(),studentBean.getEmail(),studentBean.getPassword());		
	}
}
