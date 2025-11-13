package br.com.judev.backend.feature.feed.dto;


import br.com.judev.backend.feature.authentication.dto.UserResponse;
import br.com.judev.backend.feature.feed.model.Post;

public record PostDto(String content, String picture, UserResponse userResponse) {

    public PostDto {
        picture = (picture == null || picture.isBlank()) ? null : picture.trim();
    }
    public PostDto(Post post) {
        this(post.getContent(), post.getPicture(), new UserResponse(post.getAuthor()));
    }
}

