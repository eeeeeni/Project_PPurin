package com.ppurin.ppurin.gallery;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class postService {

    private final postRepository postRepository;

    @Autowired
    public postService(postRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 모든 게시글 조회
    public List<postEntity> getAllPosts() {
        return postRepository.findAll();
    }

    // 특정 게시글 조회
    public postEntity getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    // 게시글 저장
    public postEntity savePost(postEntity post) {
        return postRepository.save(post);
    }

    // 게시글 수정
    public postEntity updatePost(Long id, postEntity updatedPost) {
        return postRepository.findById(id)
            .map(post -> {
                post.setTitle(updatedPost.getTitle());
                post.setContent(updatedPost.getContent());
                post.setTags(updatedPost.getTags());
                post.setDate(updatedPost.getDate());
                return postRepository.save(post);
            }).orElse(null);
    }
}
