package br.com.judev.backend.feature.feed.repository;

import br.com.judev.backend.feature.feed.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
