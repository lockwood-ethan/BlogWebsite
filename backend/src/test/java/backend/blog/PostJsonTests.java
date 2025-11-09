package backend.blog;

import backend.blog.entities.Post;
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

    @Test
    void postSerializationTest() throws IOException {
        Post post = new Post(23L, "Test Title", "Test Text");
        assertThat(json.write(post)).isStrictlyEqualToJson("expected.json");
        assertThat(json.write(post)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(post)).extractingJsonPathNumberValue("@.id").isEqualTo(23);
        assertThat(json.write(post)).hasJsonPathStringValue("@.title");
        assertThat(json.write(post)).extractingJsonPathStringValue("@.title").isEqualTo("Test Title");
        assertThat(json.write(post)).hasJsonPathStringValue("@.text");
        assertThat(json.write(post)).extractingJsonPathStringValue("@.text").isEqualTo("Test Text");
    }

    @Test
    void postDeserializationTest() throws IOException {
        String expected = """
                {
                    "id":23,
                    "title":"Test Title",
                    "text":"Test Text"
                }
                """;
        assertThat(json.parse(expected)).isEqualTo(new Post(23L, "Test Title", "Test Text"));
        assertThat(json.parseObject(expected).id()).isEqualTo(23);
        assertThat(json.parseObject(expected).title()).isEqualTo("Test Title");
        assertThat(json.parseObject(expected).text()).isEqualTo("Test Text");
    }
}
