package com.ppurin.ppurin.gallery;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppurin.ppurin.like.LikeRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository; // LikeRepository 선언
    private final CommentRepository commentRepository; // CommentRepository 선언

    @Autowired
    public PostService(PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;  // CommentRepository 주입
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
    public List<PostEntity> getPostsSortedByDate(String sortOrder) {
        // 날짜 정렬: "asc" - 오래된 순, "desc" - 최신순
        if ("asc".equalsIgnoreCase(sortOrder)) {
            return postRepository.findAllByOrderByDateAsc(); // 오래된 순으로 정렬
        } else {
            return postRepository.findAllByOrderByDateDesc(); // 최신순으로 정렬
        }
    }

    @Transactional
    public List<PostEntity> getPostsByTagAndSort(String tag, String sortOrder) {
        // 태그 필터와 날짜 정렬 처리
        if ("asc".equalsIgnoreCase(sortOrder)) {
            return postRepository.findByTagsContainingOrderByDateAsc(tag); // 태그 필터 + 오래된 순 정렬
        } else {
            return postRepository.findByTagsContainingOrderByDateDesc(tag); // 태그 필터 + 최신순 정렬
        }
    }

    @Transactional
    public void deletePost(Long id) {
        // 댓글 삭제
        commentRepository.deleteByPostId(id);  // 댓글을 먼저 삭제
    
        // 좋아요 삭제
        likeRepository.deleteByPostId(id);
    
        // 게시글 삭제
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("삭제하려는 게시글이 존재하지 않습니다.");
        }
        postRepository.deleteById(id);
    }

    @Transactional
    public void updatePost(Long id, PostEntity postEntity) {
        PostEntity existingPost = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        // 기존 날짜 유지
        if (postEntity.getDate() == null) {
            postEntity.setDate(existingPost.getDate()); // 수정할 날짜가 없다면 기존 날짜 유지
        }

        existingPost.setTitle(postEntity.getTitle());
        existingPost.setContent(postEntity.getContent());
        existingPost.setTags(postEntity.getTags());
        existingPost.setImageUrl(postEntity.getImageUrl());
        existingPost.setDate(postEntity.getDate());

        postRepository.save(existingPost); // 게시글 수정 후 저장
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