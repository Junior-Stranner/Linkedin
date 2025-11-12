package br.com.judev.backend.feature.feed.controller;

import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.feed.dto.CreatePostRequest;
import br.com.judev.backend.feature.feed.dto.CreatePostResponse;
import br.com.judev.backend.feature.feed.model.Post;
import br.com.judev.backend.feature.feed.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/feed")
public class PostController {

    private final FeedService feedService;

    public PostController(FeedService feedService) {
        this.feedService = feedService;
    }

    @PostMapping("/posts")
    public ResponseEntity<CreatePostResponse> createPost(
            @RequestAttribute("authenticatedUser") User user,
            @RequestPart(value = "picture", required = false) MultipartFile picture,
            @RequestPart("post") CreatePostRequest request
    ) throws Exception {
        CreatePostResponse response = feedService.createPost(picture, request, user.getEmail());
        return ResponseEntity.ok(response);
    }
}
