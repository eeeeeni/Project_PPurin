package com.ppurin.ppurin.gallery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPostId(Long postId); // 특정 게시글의 댓글 조회
        // 특정 게시글에 대한 모든 댓글 삭제
        void deleteByPostId(Long postId); // 추가된 메서드
}