package backend.blog.entities;

import org.springframework.data.annotation.Id;

public record Comment(@Id Long id, Long postId, String body, String owner) {
}
