package com.ppurin.ppurin.gallery;

public class postDTO {

    private String title;
    private String content;
    private String tags;
    private String date;

    // Getters and Setters

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // toEntity 메서드 추가
    public postEntity toEntity() {
        postEntity entity = new postEntity();
        entity.setTitle(this.title);
        entity.setContent(this.content);
        entity.setTags(this.tags);
        entity.setDate(this.date);
        return entity;
    }
}