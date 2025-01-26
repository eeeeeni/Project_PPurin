package com.ppurin.ppurin.gallery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    // 날짜별 정렬
    List<PostEntity> findAllByOrderByDateAsc();

    List<PostEntity> findAllByOrderByDateDesc();

    // 태그 필터 + 정렬 (오래된 순)
    List<PostEntity> findByTagsContainingOrderByDateAsc(String tag);

    // 태그 필터 + 정렬 (최신순)
    List<PostEntity> findByTagsContainingOrderByDateDesc(String tag);

    // 태그 필터만 (정렬 없음)
    List<PostEntity> findByTagsContaining(String tag);
}