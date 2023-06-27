package brooks.kirsten.practicalexercise.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "users")
public class User {

	@Id
	@Email
	@Column(name = "user_email", unique = true, nullable = false, updatable = false)
	private String email;

	@Column(name = "user_pass", nullable = false)
	private String password;

	@Column(name = "user_firstname")
	private String firstName;

	@Column(name = "user_lastname")
	private String lastName;

	public User() {}

	public User(User user) {
		this.email = user.getEmail().toLowerCase();
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
	}

	public User(String email, String password, String firstName, String lastName) {
		this.email = email.toLowerCase();
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email)
			&& Objects.equals(password, user.password)
			&& Objects.equals(firstName, user.firstName)
			&& Objects.equals(lastName, user.lastName);
    }

}
