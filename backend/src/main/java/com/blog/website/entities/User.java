package com.blog.website.entities;

import com.blog.website.enums.Role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;
	private String email;
	private Role role;

	protected User() {
	}

	public User(String username, String password, String email, Role role) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	@Override
	public String toString() {
		return String.format(
				"User[username='%s', email='%s']",
				username, email);
	}
}
