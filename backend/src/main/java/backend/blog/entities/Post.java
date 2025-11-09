package backend.blog.entities;

import org.springframework.data.annotation.Id;

public record Post(@Id Long id, String title, String body) {
}
