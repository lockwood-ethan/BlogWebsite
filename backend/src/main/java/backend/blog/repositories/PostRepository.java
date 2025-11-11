package backend.blog.repositories;

import backend.blog.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends CrudRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    Post findByIdAndOwner(Long id, String owner);
    Page<Post> findByOwner(String owner, PageRequest pageRequest);

    boolean existsByIdAndOwner(Long id, String owner);
}
