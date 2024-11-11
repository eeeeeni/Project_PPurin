package com.ppurin.ppurin.gallery;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gallery")
public class postController {

    private final postService postService;

    @Autowired
    public postController(postService postService) {
        this.postService = postService;
    }

    // 게시글 작성 페이지 (관리자만)
    @GetMapping("/write")
    public String write(HttpSession session, Model model) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return "gallery/write";
        }
        return "redirect:/gallery";
    }

    // 게시글 작성 처리
    @PostMapping("/write")
    public String savePost(@ModelAttribute postDTO postDto) {
        postService.savePost(postDto.toEntity());
        return "redirect:/gallery";
    }

    // 게시글 수정 페이지 (관리자만)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, HttpSession session, Model model) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            model.addAttribute("post", postService.getPostById(id));
            return "gallery/edit";
        }
        return "redirect:/gallery";
    }

    // 게시글 수정 처리
    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute postDTO postDto) {
        postService.updatePost(id, postDto.toEntity());
        return "redirect:/gallery";
    }

    // 게시글 세부 내용 보기
    @GetMapping("/inner/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "gallery/inner";
    }
}