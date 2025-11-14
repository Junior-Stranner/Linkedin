package br.com.judev.backend.feature.feed.controller;

import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.feed.dto.CommentDto;
import br.com.judev.backend.feature.feed.dto.PostDto;
import br.com.judev.backend.feature.feed.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/feed")
public class PostController {

    private final FeedService feedService;

    public PostController(FeedService feedService) {
        this.feedService = feedService;
    }

    // -----------------------------
    // POSTS
    // -----------------------------

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
            @RequestBody PostDto request
    ) throws Exception {
        var updatedPost = feedService.editPost(postId, user.getId(), null, request);
        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(feedService.getAllPosts());
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(feedService.getPost(postId));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long postId,
            @RequestAttribute("authenticatedUser") User user
    ) throws Exception {
        feedService.deletePost(postId, user.getId());
        return ResponseEntity.ok("Post deleted successfully.");
    }

    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<PostDto> likePost(
            @PathVariable Long postId,
            @RequestAttribute("authenticatedUser") User user
    ) throws Exception {
        PostDto updatedPost = feedService.likePost(postId, user.getId());
        return ResponseEntity.ok(updatedPost);
    }

    // -----------------------------
    // COMMENTS
    // -----------------------------

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long postId,
            @RequestAttribute("authenticatedUser") User user,
            @RequestBody String content
    ) {
        CommentDto dto = feedService.addComment(postId, user.getId(), content);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> editComment(
            @PathVariable Long commentId,
            @RequestAttribute("authenticatedUser") User user,
            @RequestBody String newContent
    ) {
        CommentDto dto = feedService.editComment(commentId, user.getId(), newContent);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestAttribute("authenticatedUser") User user
    ) {
        feedService.deleteComment(commentId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
