package br.com.judev.backend.feature.feed.dto;

import java.time.LocalDateTime;

public record CreatePostResponse( Long id,
         String content,
         String picture,
         Long authorId,
         String authorName,
         LocalDateTime creationDate) {
}
