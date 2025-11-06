package com.blog.website.interfaces;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.blog.website.entities.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

	List<Comment> findByPostId(long postId);

	Comment findById(long id);
}
