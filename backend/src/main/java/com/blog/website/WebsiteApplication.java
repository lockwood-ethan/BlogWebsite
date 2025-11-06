package com.blog.website;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.blog.website.enums.Role;
import com.blog.website.entities.User;
import com.blog.website.interfaces.UserRepository;

@SpringBootApplication
public class WebsiteApplication {

	private static final Logger log = LoggerFactory.getLogger(WebsiteApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebsiteApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository repository) {
		return (args) -> {
			repository.save(new User("testuser", "password1", "admin@admin.com", Role.ADMIN));
			log.info("User found with findByUsername: ");
			User user = repository.findByUserName("testuser");
			log.info(user.toString());
			log.info("");
		};
	}
}
