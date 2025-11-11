package backend.blog;

import backend.blog.entities.Post;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BlogwebsiteApplicationTests {
    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate(TestRestTemplate.HttpClientOption.ENABLE_COOKIES);

	@Test
	void shouldReturnAPostWhenDataIsSaved()  {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/posts/23", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(23);

        String title = documentContext.read("$.title");
        assertThat(title).isEqualTo("Title 1");

        String text = documentContext.read("$.body");
        assertThat(text).isEqualTo("Body 1");

        String owner = documentContext.read("$.owner");
        assertThat(owner).isEqualTo("username1");
	}

    @Test
    void shouldNotReturnAPostWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/posts/1000", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void shouldCreateANewPost() {
        Post post = new Post(null, "This title was added in a POST request", "This body was added in a POST request", "username1");
        ResponseEntity<Void> createResponse = restTemplate
                .withBasicAuth("username1", "abc123")
                .postForEntity("/posts", post, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewPost = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity(locationOfNewPost, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String title = documentContext.read("$.title");
        String body = documentContext.read("$.body");
        String owner = documentContext.read("$.owner");

        assertThat(id).isEqualTo(1);
        assertThat(title).isEqualTo("This title was added in a POST request");
        assertThat(body).isEqualTo("This body was added in a POST request");
        assertThat(owner).isEqualTo("username1");
    }

    @Test
    void shouldReturnAllPostsWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/posts", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int postCount = documentContext.read("$.length()");
        assertThat(postCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(23, 24, 25);

        JSONArray titles =  documentContext.read("$..title");
        assertThat(titles).containsExactlyInAnyOrder("Title 1", "Title 2", "Title 3");

        JSONArray bodies = documentContext.read("$..body");
        assertThat(bodies).containsExactlyInAnyOrder("Body 1", "Body 2", "Body 3");

    }

    @Test
    void shouldReturnAPageOfPosts() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/posts?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("[*]");
        assertThat(page.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnASortedPageOfPosts() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/posts?page=0&size=1&sort=id,desc", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray read =  documentContext.read("$[*]");
        assertThat(read.size()).isEqualTo(1);

        String title = documentContext.read("$[0].title");
        assertThat(title).isEqualTo("Title 3");

        String body = documentContext.read("$[0].body");
        assertThat(body).isEqualTo("Body 3");
    }

    @Test
    void shouldReturnASortedPageOfPostsWithNoParametersAndDefaultValues() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/posts", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(3);

        JSONArray titles =  documentContext.read("$..title");
        assertThat(titles).containsExactly("Title 3", "Title 2", "Title 1");

        JSONArray bodies = documentContext.read("$..body");
        assertThat(bodies).containsExactly("Body 3", "Body 2", "Body 1");
    }

    @Test
    void shouldNotReturnAPostWhenUsingBadCredentials() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("BAD-USER", "abc123")
                .getForEntity("/posts/23", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        response = restTemplate
                .withBasicAuth("username1", "BAD-PASSWORD")
                .getForEntity("/posts/23", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldRejectUsersWhoAreNotPostOwners() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("noPostOwner", "qrs456")
                .getForEntity("/posts/23", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldNotAllowAccessToPostsTheyDoNotOwn() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/posts/26", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void shouldUpdateAnExistingPost() {
        Post postUpdate = new Post(null, "This Title has been updated", "This Body has been updated", null);
        HttpEntity<Post> entity = new HttpEntity<>(postUpdate);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .exchange("/posts/23", HttpMethod.PUT, entity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/posts/23", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String title = documentContext.read("$.title");
        String body = documentContext.read("$.body");
        assertThat(id).isEqualTo(23);
        assertThat(title).isEqualTo("This Title has been updated");
        assertThat(body).isEqualTo("This Body has been updated");
    }

    @Test
    void shouldNotUpdatePostThatDoesNotExist() {
        Post unknownPost = new Post(null, "This Title has been updated", "This Body has been updated", null);
        HttpEntity<Post> entity = new HttpEntity<>(unknownPost);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .exchange("/posts/1000", HttpMethod.PUT, entity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotUpdateACashCardThatIsOwnedBySomeoneElse() {
        Post unknownPost = new Post(null, "This Title has been updated", "This Body has been updated", null);
        HttpEntity<Post> entity = new HttpEntity<>(unknownPost);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .exchange("/posts/26", HttpMethod.PUT, entity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void shouldDeleteAnExistingPost() {
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .exchange("/posts/23", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/posts/23", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotDeleteAPostThatDoesNotExist() {
        ResponseEntity<Void> deleteResponse = restTemplate
                .withBasicAuth("username1", "abc123")
                .exchange("/posts/1000", HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotAllowDeletionOfPostsTheyDoNotOwn() {
        ResponseEntity<Void> deleteResponse = restTemplate
                .withBasicAuth("username1", "abc123")
                .exchange("/posts/26", HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("newUsername", "xyz789")
                .getForEntity("/posts/26", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
