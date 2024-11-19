package com.ppurin.ppurin.gallery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface postRepository extends JpaRepository<postEntity, Long> {

    // 추가적으로 필요한 쿼리 메서드를 여기에 작성할 수 있습니다.
    // 예: 특정 제목으로 게시글 검색
    // List<postEntity> findByTitle(String title);
}