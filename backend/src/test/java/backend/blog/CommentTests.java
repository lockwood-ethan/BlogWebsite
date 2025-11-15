package backend.blog;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAllCommentsAssociatedWithPostId() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/comments/24", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int commentCount = documentContext.read("$.length()");
        assertThat(commentCount).isEqualTo(2);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(32, 33);

        JSONArray postIds = documentContext.read("$..postId");
        assertThat(postIds).containsExactlyInAnyOrder(24, 24);

        JSONArray comments = documentContext.read("$..body");
        assertThat(comments).containsExactlyInAnyOrder("Comment Body 1",  "Comment Body 2");
    }

    @Test
    void shouldReturnSavedComment() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/comments/comment/32", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldNotReturnCommentsWithUnknownPostId() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("username1", "abc123")
                .getForEntity("/comments/comment/12",  String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
