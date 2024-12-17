package com.ppurin.ppurin.like;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    List<LikeEntity> findAllByUserId(Long userId);

    void deleteByUserIdAndPostId(Long userId, Long postId);
}