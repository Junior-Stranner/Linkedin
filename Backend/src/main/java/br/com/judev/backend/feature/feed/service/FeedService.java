package br.com.judev.backend.feature.feed.service;

import br.com.judev.backend.exception.UserEmailNotFoundException;
import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.repository.UserRepository;
import br.com.judev.backend.feature.feed.model.Post;
import br.com.judev.backend.feature.feed.repository.PostRepository;
import br.com.judev.backend.feature.storage.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public Post createPost(MultipartFile picture, String content, String email) throws Exception {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("Usuário não encontrado para o email: " + email));

        String pictureUrl = null;
        if (picture != null && !picture.isEmpty()) {
            pictureUrl = storageService.saveImage(picture);
        }

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(content);
        post.setPicture(pictureUrl);

        return postRepository.save(post);
    }
}

