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

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/gallery")
public class PostController {

    private final PostService postService;
    private final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // GET 요청: /gallery (태그 필터링 및 게시글 목록)
    @GetMapping
    public String gallery(@RequestParam(value = "tag", required = false) String tag, Model model, HttpSession session) {
        // 관리자 여부 확인
        boolean isAdmin = session.getAttribute("isAdmin") != null && (Boolean) session.getAttribute("isAdmin");
        model.addAttribute("isAdmin", isAdmin);

        List<PostEntity> posts;

        // 태그가 비어있으면 모든 게시글 가져오기
        if (tag == null || tag.isEmpty()) {
            posts = postService.getAllPosts();
        } else {
            posts = postService.getPostsByTag(tag);
        }

        // PostEntity -> PostDTO 변환
        List<PostDTO> postDtos = posts.stream().map(entity -> new PostDTO(
            entity.getId(),
            entity.getTitle(),
            entity.getContent(),
            entity.getImageUrl(),
            entity.getDate(),
            entity.getTags()
        )).toList();

        model.addAttribute("posts", postDtos);

        // 모든 태그 목록 추가
        List<String> allTags = postService.getAllTags();
        model.addAttribute("allTags", allTags);

        // 현재 선택된 태그 (옵션 상태 유지)
        model.addAttribute("selectedTag", tag);

        return "gallery/gallery";
    }

    // GET 요청: /gallery/write (글쓰기 페이지 반환)
    @GetMapping("/write")
    public String writePost(Model model) {
        model.addAttribute("postDto", new PostDTO());
        return "gallery/write"; // templates/gallery/write.html 반환
    }

    // POST 요청: /gallery/write (게시글 저장)
    @PostMapping("/write")
    public String savePost(@ModelAttribute PostDTO postDto, @RequestPart("image") MultipartFile image) {
        try {
            // 디렉토리 생성
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 파일명 중복 방지를 위해 시간 추가
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String savedFilePath = uploadDir + "/" + fileName;

            // 이미지 파일 저장
            image.transferTo(new File(savedFilePath));
            postDto.setImageUrl("/uploads/" + fileName);

            // 내용이 비어 있으면 기본값 설정
            if (postDto.getContent() == null || postDto.getContent().isEmpty()) {
                postDto.setContent("Default content");
            }

            // 게시글 저장
            postService.savePost(postDto.toEntity());

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/gallery";
    }

    // GET 요청: /gallery/{id} (이너 페이지 - 게시글 상세 보기)
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
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
        return "gallery/inner"; // templates/gallery/inner.html 반환
    }
}
