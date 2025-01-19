package com.ppurin.ppurin.like;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    List<LikeEntity> findAllByUserId(Long userId);

    void deleteByUserIdAndPostId(Long userId, Long postId);

    // 새로운 메서드 추가
    void deleteByPostId(Long postId); // post_id로 참조 데이터 삭제
}