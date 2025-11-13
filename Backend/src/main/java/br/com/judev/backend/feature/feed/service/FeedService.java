package br.com.judev.backend.feature.feed.service;

import br.com.judev.backend.exception.UserEmailNotFoundException;
import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.repository.UserRepository;
import br.com.judev.backend.feature.feed.dto.PostDto;
import br.com.judev.backend.feature.feed.model.Post;
import br.com.judev.backend.feature.feed.repository.PostRepository;
import br.com.judev.backend.feature.storage.service.StorageService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;

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



}

