package com.ppurin.ppurin.gallery;

import java.util.Arrays;
import java.util.List;

public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String date;
    private String tags;
    private List<String> tagList;

    // 기본 생성자
    public PostDTO() {}

    // 생성자
    public PostDTO(Long id, String title, String content, String imageUrl, String date, String tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
        this.tags = tags;
        this.tagList = Arrays.asList(tags.split(",")); // tags를 tagList로 변환
    }

    // Getter and Setter
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
        this.tagList = Arrays.asList(tags.split(",")); // 업데이트 시 tagList 동기화
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
        this.tags = String.join(",", tagList); // 업데이트 시 tags 동기화
    }

    // Entity로 변환하는 메소드
    public PostEntity toEntity() {
        return new PostEntity(id, title, content, imageUrl, date, tags);
    }
}