package backend.blog.controllers;

import backend.blog.entities.Post;
import backend.blog.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
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

    @GetMapping()
    private ResponseEntity<List<Post>> findAll(Pageable pageable) {
        Page<Post> page = postRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, "id"))
                ));
        return ResponseEntity.ok(page.getContent());
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
