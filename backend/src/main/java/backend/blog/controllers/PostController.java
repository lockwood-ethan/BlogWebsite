package backend.blog.controllers;

import backend.blog.entities.Post;
import backend.blog.repositories.PostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;

    private PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/{requestedId}")
    private ResponseEntity<Post> findById(@PathVariable Long requestedId) {
        Optional<Post> postOptional = postRepository.findById(requestedId);
        if (postOptional.isPresent()) {
            return ResponseEntity.ok(postOptional.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createPost(@RequestBody Post newPostRequest, UriComponentsBuilder ucb) {
        Post savedPost = postRepository.save(newPostRequest);
        URI locationOfNewPost = ucb
                .path("posts/{id}")
                .buildAndExpand(savedPost.id())
                .toUri();
        return ResponseEntity.created(locationOfNewPost).build();
    }
}
