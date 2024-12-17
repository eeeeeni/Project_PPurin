package com.ppurin.ppurin.like;

import java.time.LocalDateTime;

import com.ppurin.ppurin.gallery.PostEntity;
import com.ppurin.ppurin.user.KakaoUserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_likes") // 테이블명 변경: 예약어 'likes' 충돌 방지
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private KakaoUserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 기본 생성자
    public LikeEntity() {}

    // 생성자
    public LikeEntity(KakaoUserEntity user, PostEntity post) {
        this.user = user;
        this.post = post;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public KakaoUserEntity getUser() {
        return user;
    }

    public PostEntity getPost() {
        return post;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}