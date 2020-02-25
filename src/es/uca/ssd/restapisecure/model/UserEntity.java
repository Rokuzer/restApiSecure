package es.uca.ssd.restapisecure.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import es.uca.ssd.restapisecure.generator.SecureIdentifierGenerator;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@NamedQueries({ @NamedQuery(name = "User.allUsers", query = "SELECT u FROM UserEntity u"),
		@NamedQuery(name = "User.allUsersOrderedByUsername", query = "SELECT u FROM UserEntity u ORDER BY username ASC"),
		@NamedQuery(name = "User.findByUsername", query = "SELECT u FROM UserEntity u WHERE username LIKE :username"),
		@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM UserEntity u WHERE email LIKE :email") })
public class UserEntity implements Serializable {

	@Id
	@GeneratedValue(generator = SecureIdentifierGenerator.GENERATOR_NAME)
	@GenericGenerator(name = SecureIdentifierGenerator.GENERATOR_NAME, strategy = "es.uca.ssd.restapisecure.generator.SecureIdentifierGenerator")
	private String id;

	@NotNull
	@Size(min = 3, max = 25)
	private String username;

	@NotNull
	@Size(min = 6, max = 25)
	private String password;

	@NotNull
	@Size(min = 3, max = 25)
	@Pattern(regexp = "^[A-Za-z ]+$", message = "Must not contain numbers or symbols")
	private String name;

	@NotNull
	@Size(min = 3, max = 25)
	@Pattern(regexp = "^[A-Za-z ]+$", message = "Must not contain numbers or symbols")
	private String surname;

	@NotNull
	@NotEmpty
	@Email
	@Column(unique = true)
	private String email;

	private String apiKey;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "enrolled_courses", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	private Set<CourseEntity> courses;

	public UserEntity() {
		this.courses = new HashSet<>();
	}

	public UserEntity(String username, String password, String name, String surname, String email) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.courses = new HashSet<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Set<CourseEntity> getCourses() {
		return courses;
	}

	public void setCourses(Set<CourseEntity> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name
				+ ", surname=" + surname + ", email=" + email + ", apiKey=" + apiKey + ", courses=" + courses + "]";
	}

}
