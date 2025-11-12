package br.com.judev.backend.feature.feed.dto;

import br.com.judev.backend.feature.feed.model.Post;

import java.time.LocalDateTime;

public record CreatePostResponse( Long id,
         String content,
         String picture,
         Long authorId,
         String authorName,
         LocalDateTime creationDate) {
    public CreatePostResponse(Post post) {
        this(
                post.getId(),
                post.getContent(),
                post.getPicture(),
                post.getAuthor().getId(),
                post.getAuthor().getFirstName() + " " + post.getAuthor().getLastName(),
                post.getCreationDate()
        );
    }
}
