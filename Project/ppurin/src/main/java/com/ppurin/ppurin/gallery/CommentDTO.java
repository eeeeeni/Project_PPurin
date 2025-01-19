package com.ppurin.ppurin.gallery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 초기화하는 생성자
public class CommentDTO {

    private Long id;
    private Long postId;
    private String content;
    private String author;
    private String createdAt;
}