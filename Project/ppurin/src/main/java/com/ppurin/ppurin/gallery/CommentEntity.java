package com.ppurin.ppurin.gallery;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter // Lombok으로 모든 필드에 대한 setter 생성
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String createdAt;

    public CommentEntity(PostEntity post, String content, String author, String createdAt) {
        this.post = post;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }
}