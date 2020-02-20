package es.uca.ssd.restapisecure.model;

import java.io.Serializable;

import javax.persistence.Column;
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
public class UserEntity implements Serializable {

	@Id
	@GeneratedValue
	private int id;

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
	// @NotEmpty
	// @Email
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
