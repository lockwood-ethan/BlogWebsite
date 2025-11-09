package backend.blog;

import backend.blog.entities.Post;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogwebsiteApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

	@Test
	void shouldReturnAPostWhenDataIsSaved()  {
        ResponseEntity<String> response = restTemplate.getForEntity("/posts/23", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(23);

        String title = documentContext.read("$.title");
        assertThat(title).isEqualTo("Test Title");

        String text = documentContext.read("$.body");
        assertThat(text).isEqualTo("Test Body");
	}

    @Test
    void shouldNotReturnAPostWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/posts/1000", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldCreateANewPost() {
        Post post = new Post(null, "This title was added in a POST request", "This body was added in a POST request");
        ResponseEntity<Void> createResponse = restTemplate.postForEntity("/posts", post, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewPost = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewPost, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String title = documentContext.read("$.title");
        String body = documentContext.read("$.body");

        assertThat(id).isEqualTo(1);
        assertThat(title).isEqualTo("This title was added in a POST request");
        assertThat(body).isEqualTo("This body was added in a POST request");
    }
}
