package backend.blog.controllers;

import backend.blog.entities.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

    @GetMapping("/{requestedId}")
    private ResponseEntity<Post> findById(@PathVariable String requestedId) {
        if (requestedId.equals("23")) {
            Post post = new Post(23L, "Test", "Test");
            return ResponseEntity.ok(post);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
