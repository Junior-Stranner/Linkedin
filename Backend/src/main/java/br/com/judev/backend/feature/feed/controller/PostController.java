package br.com.judev.backend.feature.feed.controller;

import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.feed.dto.PostDto;
import br.com.judev.backend.feature.feed.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/feed")
public class PostController {

    private final FeedService feedService;

    public PostController(FeedService feedService) {
        this.feedService = feedService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(
            @RequestAttribute("authenticatedUser") User user,
            @RequestBody PostDto request
    ) throws Exception {
        var postResponse = feedService.createPost(null, request, user.getEmail());
        return ResponseEntity.ok(postResponse);
    }

}
