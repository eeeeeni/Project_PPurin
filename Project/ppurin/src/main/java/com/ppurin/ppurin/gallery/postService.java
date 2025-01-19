package com.ppurin.ppurin.gallery;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppurin.ppurin.like.LikeRepository; // LikeRepository 임포트 필요

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository; // LikeRepository 선언

    @Autowired
    public PostService(PostRepository postRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
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

    @Transactional
    public void deletePost(Long id) {
        // 참조 데이터 먼저 삭제
        likeRepository.deleteByPostId(id);

        // 게시글 삭제
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("삭제하려는 게시글이 존재하지 않습니다.");
        }
        postRepository.deleteById(id);
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