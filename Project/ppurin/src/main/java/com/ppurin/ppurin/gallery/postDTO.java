package com.ppurin.ppurin.gallery;

import java.util.List;

public class postDTO {
    private String title;
    private String content;
    private String imageUrl;
    private String date;
    private String tags;

    // 추가된 필드
    private List<String> tagList;

    // 기본 생성자
    public postDTO() {}

    // 생성자
    public postDTO(String title, String content, String imageUrl, String date, String tags) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
        this.tags = tags;
    }

    // Getter and Setter
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    // 추가된 Getter and Setter for tagList
    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    // Entity로 변환하는 메소드
    public postEntity toEntity() {
        return new postEntity(title, content, imageUrl, date, tags);
    }
}