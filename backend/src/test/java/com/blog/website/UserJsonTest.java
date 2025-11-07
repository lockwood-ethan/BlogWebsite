package com.blog.website;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import com.blog.website.enums.Role;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserJsonTest {

	@Test
	void randomTest() {
		assertThat(1).isEqualTo(42);
	}
	// @Autowired
	// private JacksonTester<User> json;
	//
	// @Test
	// void userSerializationTest() throws IOException {
	// User user = new User("username", "password1", "admin@admin1.com",
	// Role.ADMIN);
	//
	// assertThat(json.write(user)).isStrictlyEqualToJson("expected.json");
	// assertThat(json.write(user)).hasJsonPathNumberValue("@.id");
	// assertThat(json.write(user)).extractingJsonPathNumberValue("@.id").isEqualTo(1);
	// assertThat(json.write(user)).hasJsonPathStringValue("@.username");
	// assertThat(json.write(user)).extractingJsonPathStringValue("@.username").isEqualTo("username");
	// assertThat(json.write(user)).hasJsonPathStringValue("@.password");
	// assertThat(json.write(user)).extractingJsonPathStringValue("@.password").isEqualTo("password1");
	// assertThat(json.write(user)).hasJsonPathStringValue("@.email");
	// assertThat(json.write(user)).extractingJsonPathStringValue("@.email").isEqualTo("admin@admin1.com");
	// assertThat(json.write(user)).hasJsonPathValue("@.role");
	// assertThat(json.write(user)).extractingJsonPathValue("@.role").isEqualTo(Role.ADMIN);
	// }
	//
	// @Test
	// void userDeserializationTest() throws IOException {
	// String expected = """
	// {
	// "id":1,
	// "username":"username",
	// "password":"password1",
	// "email":"admin@admin1.com",
	// "role":Role.ADMIN
	// } """;
	// assertThat(json.parse(expected))
	// .isEqualTo(new User("username", "password1", "admin@admin1.com",
	// Role.ADMIN));
	// assertThat(json.parseObject(expected).getId()).isEqualTo(1);
	// assertThat(json.parseObject(expected).getUsername()).isEqualTo("username");
	// assertThat(json.parseObject(expected).getPassword()).isEqualTo("password1");
	// assertThat(json.parseObject(expected).getEmail()).isEqualTo("admin@admin1.com");
	// assertThat(json.parseObject(expected).getRole()).isEqualTo(Role.ADMIN);
	// }
}
