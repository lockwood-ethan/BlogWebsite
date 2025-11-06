package com.blog.website.interfaces;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.blog.website.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);

	User findById(long id);
}
