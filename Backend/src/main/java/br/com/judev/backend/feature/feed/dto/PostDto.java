package br.com.judev.backend.feature.feed.dto;


import br.com.judev.backend.feature.authentication.dto.UserResponse;
import br.com.judev.backend.feature.feed.model.Post;

import java.time.LocalDateTime;

public record PostDto(
        String content,
        String picture,
        UserResponse author,
        LocalDateTime creationDate,
        LocalDateTime updatedDate
) {

    public PostDto {
        picture = (picture == null || picture.isBlank()) ? null : picture.trim();
    }

    public PostDto(Post post) {
        this(
                post.getContent(),
                post.getPicture(),
                new UserResponse(post.getAuthor()),
                post.getCreationDate(),
                post.getUpdatedDate()
        );
    }
}
