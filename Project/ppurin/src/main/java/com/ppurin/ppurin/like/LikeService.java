package com.ppurin.ppurin.like;

import java.util.List;
import java.util.stream.Collectors;

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

    // 좋아요 토글 메서드
    @Transactional
    public boolean toggleLike(Long userId, Long postId) {
        KakaoUserEntity user = kakaoRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));

        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            // 이미 좋아요한 경우 삭제
            likeRepository.deleteByUserIdAndPostId(userId, postId);
            return false; // 좋아요 취소
        } else {
            // 좋아요 추가
            LikeEntity like = new LikeEntity(user, post);
            likeRepository.save(like);
            return true; // 좋아요 등록
        }
    }

    // 좋아요한 게시글 가져오기
    @Transactional(readOnly = true)
    public List<LikeDTO> getLikedPostsByUser(Long userId) {
        return likeRepository.findAllByUserId(userId).stream()
                .filter(like -> like.getPost() != null) // PostEntity가 존재하는 경우만 반환
                .map(like -> new LikeDTO(
                        like.getId(),
                        like.getUser().getId(),
                        like.getPost().getId(), // 존재하는 PostEntity의 ID
                        like.getPost().getTitle(),
                        like.getPost().getImageUrl(),
                        like.getPost().getDate() // PostEntity에서 LocalDate로 저장된 날짜를 가져옴
                ))
                .collect(Collectors.toList());
    }
}