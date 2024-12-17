package com.ppurin.ppurin.gallery;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    public PostEntity getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    public PostEntity savePost(PostEntity post) {
        return postRepository.save(post);
    }

    public PostEntity updatePost(Long id, PostEntity updatedPost) {
        return postRepository.findById(id)
            .map(post -> {
                post.setTitle(updatedPost.getTitle());
                post.setContent(updatedPost.getContent());
                post.setTags(updatedPost.getTags());
                post.setDate(updatedPost.getDate());
                post.setImageUrl(updatedPost.getImageUrl());
                return postRepository.save(post);
            }).orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    public List<PostEntity> getPostsByTag(String tag) {
        return postRepository.findByTagsContaining(tag);
    }

    public List<String> getAllTags() {
        return postRepository.findAll().stream()
            .filter(post -> post.getTags() != null && !post.getTags().isEmpty())
            .flatMap(post -> Arrays.stream(post.getTags().split(",")))
            .map(String::trim)
            .filter(tag -> !tag.isEmpty())
            .collect(Collectors.toSet())
            .stream()
            .sorted()
            .toList();
    }
}
