package br.com.judev.backend.feature.feed.repository;

import br.com.judev.backend.feature.feed.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorId(Long authorId);

    List<Post> findAllByOrderByCreationDateDesc();

    List<Post> findByAuthorIdInOrderByCreationDateDesc(Set<Long> connectedUserIds);
}
