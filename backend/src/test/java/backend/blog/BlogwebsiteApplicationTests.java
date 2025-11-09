package backend.blog;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        assertThat(title).isEqualTo("Test");

        String text = documentContext.read("$.text");
        assertThat(text).isEqualTo("Test");
	}

    @Test
    void shouldNotReturnAPostWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/posts/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }
}
