package br.com.judev.backend.feature.feed.controller;

import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.feed.dto.PostDto;
import br.com.judev.backend.feature.feed.model.Post;
import br.com.judev.backend.feature.feed.repository.PostRepository;
import br.com.judev.backend.feature.feed.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/feed")
public class PostController {

    private final FeedService feedService;
    private final PostRepository postRepository;

    public PostController(FeedService feedService, PostRepository postRepository) {
        this.feedService = feedService;
        this.postRepository = postRepository;
    }


    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(
            @RequestAttribute("authenticatedUser") User user,
            @RequestBody PostDto request
    ) throws Exception {
        var postResponse = feedService.createPost(null, request, user.getEmail());
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long postId,
            @RequestAttribute("authenticatedUser") User user,
            @RequestBody PostDto request) throws Exception {
        var updatedPost = feedService.editPost(postId, user.getId(), null, request);
        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = feedService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        PostDto PostDto = feedService.getPost(postId);
        return ResponseEntity.ok(PostDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long postId,
            @RequestAttribute("authenticatedUser") User user
    ) throws Exception {
        feedService.deletePost(postId, user.getId());
        return ResponseEntity.ok("Post deleted successfully.");
    }

    @PutMapping("/{postId}/like")
    public ResponseEntity<PostDto> likePost(
            @PathVariable Long postId,
            @RequestAttribute("authenticatedUser") User user
    ) throws Exception {
        PostDto updatedPost = feedService.likePost(postId, user.getId());
        return ResponseEntity.ok(updatedPost);
    }
}
