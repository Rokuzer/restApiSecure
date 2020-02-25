package es.uca.ssd.restapisecure.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(name = "Course", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class CourseEntity implements Serializable {

	@Id
	@GeneratedValue
	private int id;

	@NotNull
	@Size(min = 3, max = 25)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	private String name;

	@NotNull
	@Size(min = 3, max = 255)
	private String description;

	@NotNull
	private Integer time;

	@NotNull
	private Integer price;

	@ManyToMany
	private Set<UserEntity> users;

	public CourseEntity() {
		this.users = new HashSet<>();
	}

	public CourseEntity(String name, String description, Integer time) {
		this.name = name;
		this.description = description;
		this.time = time;
		this.users = new HashSet<>();
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "CourseEntity [id=" + id + ", name=" + name + ", description=" + description + ", time=" + time
				+ ", price =" + price + "]";
	}

}
