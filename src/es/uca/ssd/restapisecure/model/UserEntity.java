package es.uca.ssd.restapisecure.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@NamedQueries({ @NamedQuery(name = "User.allUsers", query = "SELECT u FROM User u"),
		@NamedQuery(name = "User.allUsersOrderedByUsername", query = "SELECT u FROM User u ORDER BY username ASC"),
		@NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE username LIKE :username"),
		@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE email LIKE :email") })
public class UserEntity implements Serializable {

	@Id
	@GeneratedValue
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@NotNull
	@Size(min = 3, max = 25)
	private String username;

	@NotNull
	@Size(min = 6, max = 25)
	private String password;

	@NotNull
	@Size(min = 3, max = 25)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	private String name;

	@NotNull
	@Size(min = 3, max = 25)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	private String surname;

	@NotNull
	@NotEmpty
	@Email
	@Column(unique = true)
	private String email;

	private String apiKey;

	public UserEntity() {
	}

	public UserEntity(String username, String password, String name, String surname, String email) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.email = email;
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

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name
				+ ", surname=" + surname + ", email=" + email + ", apiKey=" + apiKey + "]";
	}

}
