package com.blog.website.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	private Long postId;
	private String text;

	protected Comment() {
	}

	public Comment(Long userId, Long postId, String text) {
		this.userId = userId;
		this.postId = postId;
		this.text = text;
	}
}
