package com.example.lab1.controller;

import com.example.lab1.dto.PostRequest;
import com.example.lab1.entity.PostEntity;
import com.example.lab1.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {
    private final PostService postService;

    public DataController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/data")
    public ResponseEntity<List<PostEntity>> getData() {
        return ResponseEntity.ok(postService.findAll());
    }

    @PostMapping("/data")
    public ResponseEntity<PostEntity> createPost(@Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.createPost(request.getTitle(), request.getContent()));
    }

    @GetMapping("/health")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminOnly() {
        return ResponseEntity.ok().build();
    }
}
