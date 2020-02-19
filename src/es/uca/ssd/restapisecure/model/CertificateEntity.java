package es.uca.ssd.restapisecure.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
public class CertificateEntity implements Serializable {

	@Id
	@GeneratedValue
	private int id;

	@NotNull
	@Size(min = 3, max = 25)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	private String course;


	@NotNull
	@Size(min = 3, max = 25)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	private String annotation;
	
	@NotNull
	//@Size(min = 3, max = 255)
	//@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	private Integer note;
	
	//Time in hours
	@NotNull
	// @NotEmpty
	//@Column(unique = true)
	private boolean pass;

	public CertificateEntity() {
	}

	public CertificateEntity(String course,String annotation, Integer note, boolean pass) {
		this.course = course;
		this.annotation = annotation;
		this.note = note;
		this.pass = pass;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public Integer getNote() {
		return note;
	}

	public void setNote(Integer note) {
		this.note = note;
	}

	public Boolean getPass() {
		return pass;
	}

	public void setPass(Boolean pass) {
		this.pass = pass;
	}
	
	@Override
	public String toString() {
		return "CertificateEntity [id=" + id + ", course=" + course + ", annotation=" + annotation + ", note=" + note + ",pass="+ pass+"]";
	}

}
