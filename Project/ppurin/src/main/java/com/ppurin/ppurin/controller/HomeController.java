package com.ppurin.ppurin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/index")
    public String index(Model model) {
        // 홈 페이지로 이동
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        // about.html 파일로 이동
        return "about";
    }
    
    @GetMapping("/gallery")
    public String gallery(Model model) {
        // "gallery.html" 파일로 이동
        return "gallery/gallery";
    }


    @GetMapping("/gallery/inner")
    public String innerGallery(Model model) {
        // "inner.html" 파일로 이동
        return "gallery/inner";
    }

}