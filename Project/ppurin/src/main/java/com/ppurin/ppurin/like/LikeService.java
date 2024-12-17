package com.ppurin.ppurin.like;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppurin.ppurin.gallery.PostEntity;
import com.ppurin.ppurin.gallery.PostRepository;
import com.ppurin.ppurin.user.KakaoRepository;
import com.ppurin.ppurin.user.KakaoUserEntity;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final KakaoRepository kakaoRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, KakaoRepository kakaoRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.kakaoRepository = kakaoRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public void likePost(Long userId, Long postId) {
        KakaoUserEntity user = kakaoRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new IllegalArgumentException("Already liked this post.");
        }

        LikeEntity like = new LikeEntity(user, post);
        likeRepository.save(like);
    }

    @Transactional(readOnly = true)
    public List<LikeDTO> getLikedPostsByUser(Long userId) {
        return likeRepository.findAllByUserId(userId).stream()
                .map(like -> new LikeDTO(
                        like.getId(),
                        like.getUser().getId(),
                        like.getPost().getId(),
                        like.getPost().getTitle(),
                        like.getPost().getImageUrl(),
                        like.getPost().getDate() // 추가
                ))
                .toList();
    }
}