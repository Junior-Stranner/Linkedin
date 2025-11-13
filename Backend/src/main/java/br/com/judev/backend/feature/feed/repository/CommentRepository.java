package br.com.judev.backend.feature.feed.repository;

import br.com.judev.backend.feature.feed.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
