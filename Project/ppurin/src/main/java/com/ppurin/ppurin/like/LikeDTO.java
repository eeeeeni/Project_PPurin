package com.ppurin.ppurin.like;

public class LikeDTO {
    private Long id;          // Like Entity의 ID
    private Long userId;      // 유저 ID
    private Long postId;      // 게시물 ID
    private String postTitle; // 게시물 제목
    private String postImageUrl; // 게시물 이미지 URL
    private String date;      // 게시물 촬영 날짜

    public LikeDTO(Long id, Long userId, Long postId, String postTitle, String postImageUrl, String date) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.postTitle = postTitle;
        this.postImageUrl = postImageUrl;
        this.date = date;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPostId() {
        return postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public String getDate() {
        return date;
    }
}