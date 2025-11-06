package com.blog.website.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "posts")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	private String title;
	private String text;

	protected Post() {
	}

	public Post(Long userId, String title, String text) {
		this.userId = userId;
		this.title = title;
		this.text = text;
	}
}
