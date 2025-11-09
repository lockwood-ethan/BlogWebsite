package backend.blog.repositories;

import backend.blog.entities.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends CrudRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
}
