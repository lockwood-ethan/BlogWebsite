package com.blog.website.interfaces;

import org.springframework.data.repository.CrudRepository;

import com.blog.website.entities.Post;

public interface PostRepository extends CrudRepository<Post, Long> {

	Post findById(long id);
}
