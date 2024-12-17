package com.ppurin.ppurin.like;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ppurin.ppurin.user.KakaoUserDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // 마이페이지 렌더링
    @GetMapping("/mypage")
    public String mypageUser(HttpSession session, Model model) {
        KakaoUserDTO user = (KakaoUserDTO) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<LikeDTO> likedPosts = likeService.getLikedPostsByUser(user.getId());
        model.addAttribute("likedPosts", likedPosts);

        return "user/mypage"; // user/mypage.html 반환
    }

    // 좋아요한 게시물 데이터 반환 (JSON)
    @GetMapping("/mypage/data")
    public ResponseEntity<List<LikeDTO>> getLikedPosts(HttpSession session) {
        KakaoUserDTO user = (KakaoUserDTO) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }

        List<LikeDTO> likedPosts = likeService.getLikedPostsByUser(user.getId());
        return ResponseEntity.ok(likedPosts);
    }

    // 좋아요 기능
    @PostMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId, HttpSession session) {
        KakaoUserDTO user = (KakaoUserDTO) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in.");
        }

        try {
            likeService.likePost(user.getId(), postId);
            return ResponseEntity.ok("Liked the post.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error while liking the post.");
        }
    }


}