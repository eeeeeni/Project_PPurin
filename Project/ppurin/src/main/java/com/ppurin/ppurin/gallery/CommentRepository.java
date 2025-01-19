package com.ppurin.ppurin.gallery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPostId(Long postId); // 특정 게시글의 댓글 조회
}