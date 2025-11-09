package backend.blog;

import backend.blog.entities.Post;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class PostJsonTests {

    @Autowired
    private JacksonTester<Post> json;

    @Autowired
    private JacksonTester<Post[]> jsonList;

    private Post[] posts;

    @BeforeEach
    void setUp() {
        posts = Arrays.array(
                new Post(23L, "Title 1", "Body 1"),
                new Post(24L, "Title 2", "Body 2"),
                new Post(25L, "Title 3", "Body 3"));
    }

    @Test
    void postSerializationTest() throws IOException {
        Post post = new Post(23L, "Test Title", "Test Body");
        assertThat(json.write(post)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(post)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(post)).extractingJsonPathNumberValue("@.id").isEqualTo(23);
        assertThat(json.write(post)).hasJsonPathStringValue("@.title");
        assertThat(json.write(post)).extractingJsonPathStringValue("@.title").isEqualTo("Test Title");
        assertThat(json.write(post)).hasJsonPathStringValue("@.body");
        assertThat(json.write(post)).extractingJsonPathStringValue("@.body").isEqualTo("Test Body");
    }

    @Test
    void postListSerializationTest() throws IOException {
        assertThat(jsonList.write(posts)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void postDeserializationTest() throws IOException {
        String expected = """
                {
                    "id":23,
                    "title":"Test Title",
                    "body":"Test Body"
                }
                """;
        assertThat(json.parse(expected)).isEqualTo(new Post(23L, "Test Title", "Test Body"));
        assertThat(json.parseObject(expected).id()).isEqualTo(23);
        assertThat(json.parseObject(expected).title()).isEqualTo("Test Title");
        assertThat(json.parseObject(expected).body()).isEqualTo("Test Body");
    }

    @Test
    void postListDeserializationTest() throws IOException {
        String expected = """
                [
                    { "id": 23, "title": "Title 1", "body": "Body 1" },
                    { "id": 24, "title": "Title 2", "body": "Body 2" },
                    { "id": 25, "title": "Title 3", "body": "Body 3" }
                ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(posts);
    }
}
