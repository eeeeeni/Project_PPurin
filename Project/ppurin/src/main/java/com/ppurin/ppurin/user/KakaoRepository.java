package com.ppurin.ppurin.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoRepository extends JpaRepository<KakaoUserEntity, Long> {
    KakaoUserEntity findByNickname(String nickname);
}