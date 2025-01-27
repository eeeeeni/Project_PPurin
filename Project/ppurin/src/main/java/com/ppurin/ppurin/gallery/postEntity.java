package com.ppurin.ppurin.gallery;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "post_entity") // 데이터베이스의 테이블 이름과 매핑
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 전략
    private Long id;

    @Column(nullable = false) // 컬럼 매핑 및 제약조건 추가
    private String title;

    @Column(nullable = false, length = 1000) // 길이 제약 조건 추가
    private String content;

    @Column(name = "image_url") // 컬럼 이름과 필드 이름 다를 경우 매핑
    private String imageUrl;

    @Column // 날짜 필드 매핑
    private LocalDate date;

    @Column // 태그 필드 매핑
    private String tags;

    // 기본 생성자
    public PostEntity() {}

    // 전체 필드를 초기화하는 생성자
    public PostEntity(Long id, String title, String content, String imageUrl, LocalDate date, String tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
        this.tags = tags;
    }

    // Getter와 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}