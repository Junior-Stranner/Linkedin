package br.com.judev.backend.feature.feed.service;

import br.com.judev.backend.exception.UserEmailNotFoundException;
import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.repository.UserRepository;
import br.com.judev.backend.feature.feed.model.Post;
import br.com.judev.backend.feature.feed.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FeedService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public FeedService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(MultipartFile picture, String content, String email) throws Exception {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("User not found"));

        String pictureUrl = storageService.saveImage(picture);

        return postRepository.save(author);
    }
}
