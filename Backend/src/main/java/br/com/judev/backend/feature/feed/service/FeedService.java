package br.com.judev.backend.feature.feed.service;

import br.com.judev.backend.exception.UserEmailNotFoundException;
import br.com.judev.backend.feature.authentication.dto.UserResponse;
import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.repository.UserRepository;
import br.com.judev.backend.feature.feed.dto.PostDto;
import br.com.judev.backend.feature.feed.model.Post;
import br.com.judev.backend.feature.feed.repository.PostRepository;
import br.com.judev.backend.feature.storage.service.StorageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FeedService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;

    public FeedService(PostRepository postRepository, UserRepository userRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    @Transactional
    public PostDto createPost(MultipartFile picture, PostDto request, String email) throws Exception {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("User not found by e-mail: " + email));

        String pictureUrl = null;

        // Se vier um arquivo, salva e usa o link do storage
        if (picture != null && !picture.isEmpty()) {
            pictureUrl = storageService.saveImage(picture);
        }
        // Se não vier arquivo, mas vier um link no JSON
        else if (request.picture() != null && !request.picture().isBlank()) {
            pictureUrl = request.picture();
        }

        Post post = new Post(request.content(), author);
        post.setPicture(pictureUrl);
        post.setLikes(new HashSet<>());

        Post savedPost = postRepository.save(post);
        return new PostDto(savedPost);
    }


    @Transactional
    public PostDto editPost(Long postId, Long userId, MultipartFile picture, PostDto request) throws Exception {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        //Garante que o post pertence ao usuário autenticado
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not the author of the post");
        }

        //Atualiza o conteúdo se vier no request
        if (request.content() != null && !request.content().isBlank()) {
            post.setContent(request.content());
        }

        String pictureUrl = post.getPicture(); // mantém a atual por padrão

        if (picture != null && !picture.isEmpty()) {
            pictureUrl = storageService.saveImage(picture);
        } else if (request.picture() != null && !request.picture().isBlank()) {
            pictureUrl = request.picture();
        }

        post.setPicture(pictureUrl);
        post.setUpdatedDate(LocalDateTime.now());
        Post updatedPost = postRepository.save(post);
        return new PostDto(updatedPost);
    }

    public PostDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        return new PostDto(post);
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findAllByOrderByCreationDateDesc()
                .stream()
                .map(PostDto::new)
                .toList();
    }

    public Set<UserResponse> getPostLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return post.getLikes()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toSet());
    }

}

