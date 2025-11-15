package backend.blog.repositories;

import backend.blog.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommentRepository extends CrudRepository<Comment, Long>, PagingAndSortingRepository<Comment, Long> {
    Comment findCommentById(Long id);
    Page<Comment> findByPostId(Long postId, PageRequest pagerequest);
}
