package com.example.lab1.service;

import com.example.lab1.entity.PostEntity;
import com.example.lab1.repository.PostRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @PreAuthorize("isAuthenticated()")
    public PostEntity createPost(String title, String content) {
        PostEntity post = new PostEntity();
        post.setTitle(sanitize(title));
        post.setContent(sanitize(content));
        post.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return postRepository.save(post);
    }

    public List<PostEntity> findAll() {
        return postRepository.findAll();
    }

    private String sanitize(String value) {
        return Jsoup.clean(value, Safelist.none());
    }
}
