package com.ppurin.ppurin.gallery;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ppurin.ppurin.like.LikeRepository;
import com.ppurin.ppurin.user.KakaoUserDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/gallery")
public class PostController {

    private final PostService postService;
    private final CommentService commentService; // 댓글 서비스 추가
    private final LikeRepository likeRepository; // 좋아요 상태 확인을 위해 추가
    private final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

    public PostController(PostService postService, CommentService commentService, LikeRepository likeRepository) {
        this.postService = postService;
        this.commentService = commentService;
        this.likeRepository = likeRepository;
    }

    // GET 요청: /gallery (태그 필터링 및 게시글 목록)
    @GetMapping
    public String gallery(@RequestParam(value = "tag", required = false) String tag, Model model, HttpSession session) {
        boolean isAdmin = session.getAttribute("isAdmin") != null && (Boolean) session.getAttribute("isAdmin");
        model.addAttribute("isAdmin", isAdmin);

        // 세션에서 사용자 정보 가져오기
        final Long userId; // userId를 final로 선언
        Object userObj = session.getAttribute("user");
        if (userObj instanceof KakaoUserDTO user) {
            userId = user.getId(); // userId를 final로 초기화
        } else {
            userId = null; // userId를 초기화
        }

        // 태그에 따라 게시글 필터링
        List<PostEntity> posts;
        if (tag == null || tag.isEmpty()) {
            posts = postService.getAllPosts();
        } else {
            posts = postService.getPostsByTag(tag);
        }

        // 게시글 DTO 리스트로 변환 (좋아요 상태 포함)
        List<PostDTO> postDtos = posts.stream()
            .map(entity -> new PostDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getImageUrl(),
                entity.getDate(),
                entity.getTags(),
                userId != null && likeRepository.existsByUserIdAndPostId(userId, entity.getId()) // 좋아요 여부 확인
            ))
            .toList();

        model.addAttribute("posts", postDtos);

        // 태그 리스트 전달
        List<String> allTags = postService.getAllTags();
        model.addAttribute("allTags", allTags);
        model.addAttribute("selectedTag", tag);

        return "gallery/gallery";
    }

    // GET 요청: /gallery/write (글쓰기 페이지 반환)
    @GetMapping("/write")
    public String writePost(Model model) {
        model.addAttribute("postDto", new PostDTO());
        return "gallery/write";
    }

    // POST 요청: /gallery/write (게시글 저장)
    @PostMapping("/write")
    public String savePost(@ModelAttribute PostDTO postDto, @RequestPart("image") MultipartFile image) {
        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String savedFilePath = uploadDir + "/" + fileName;
            image.transferTo(new File(savedFilePath));
            postDto.setImageUrl("/uploads/" + fileName);

            if (postDto.getContent() == null || postDto.getContent().isEmpty()) {
                postDto.setContent("Default content");
            }

            postService.savePost(postDto.toEntity());
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/gallery";
    }

    // GET 요청: /gallery/{id} (이너 페이지 - 게시글 상세 보기)
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model, HttpSession session) {
        PostEntity postEntity = postService.getPostById(id);
        PostDTO postDto = new PostDTO(
            postEntity.getId(),
            postEntity.getTitle(),
            postEntity.getContent(),
            postEntity.getImageUrl(),
            postEntity.getDate(),
            postEntity.getTags()
        );

        model.addAttribute("post", postDto);

        boolean isAdmin = session.getAttribute("isAdmin") != null && (Boolean) session.getAttribute("isAdmin");
        model.addAttribute("isAdmin", isAdmin);

        // 댓글 데이터 추가
        List<CommentEntity> comments = commentService.getCommentsByPostId(id);
        model.addAttribute("comments", comments);

        return "gallery/inner";
    }

    // POST 요청: /gallery/delete/{id} (게시글 삭제)
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        boolean isAdmin = session.getAttribute("isAdmin") != null && (Boolean) session.getAttribute("isAdmin");
        if (!isAdmin) {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제 권한이 없습니다.");
            return "redirect:/gallery";
        }
        System.out.println("삭제 요청 처리 중: Post ID = " + id);

        try {
            postService.deletePost(id);
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/gallery";
    }

    // POST 요청: /gallery/{postId}/comments (댓글 추가)
    @PostMapping("/{postId}/comments")
    public String addComment(@PathVariable Long postId, @RequestParam String content, HttpSession session, RedirectAttributes redirectAttributes) {
        String author = (String) session.getAttribute("nickname"); // 세션에서 닉네임 가져오기
        System.out.println("작성자: " + author + ", 댓글 내용: " + content); // 디버깅 로그
    
        if (author == null || author.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 후 댓글을 작성할 수 있습니다.");
            return "redirect:/gallery/" + postId;
        }
    
        PostEntity post = postService.getPostById(postId);
        commentService.addComment(post, content, author);
        redirectAttributes.addFlashAttribute("successMessage", "댓글이 추가되었습니다.");
        return "redirect:/gallery/" + postId;
    }

    // POST 요청: /gallery/comments/{commentId}/delete (댓글 삭제)
    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, RedirectAttributes redirectAttributes) {
        commentService.deleteComment(commentId);
        redirectAttributes.addFlashAttribute("successMessage", "댓글이 삭제되었습니다.");
        return "redirect:/gallery";
    }
}