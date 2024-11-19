
package com.ppurin.ppurin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ppurin.ppurin.user.KakaoUserDTO;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        // 세션에서 사용자 닉네임 확인
        String nickname = (String) session.getAttribute("nickname");
        model.addAttribute("nickname", nickname); // 닉네임 추가
        return "index"; // index.html 출력
    }

    @GetMapping("/about")
    public String about(Model model) {
        return "about"; // about.html 출력
    }
    
    // @GetMapping("/gallery")
    // public String gallery(Model model) {
    //     return "gallery/gallery"; // gallery.html 출력
    // }

    // @GetMapping("/gallery/inner")
    // public String innerGallery(Model model) {
    //     return "gallery/inner"; // inner.html 출력
    // }

@GetMapping("/user/mypage")
public String mypageUser(HttpSession session, Model model) {
    // 세션에서 사용자 정보 확인
    KakaoUserDTO user = (KakaoUserDTO) session.getAttribute("user");

    // 세션에 사용자 정보가 없을 경우
    if (user == null) {
        // System.out.println("세션에 사용자 정보 없음. 로그인 페이지로 리다이렉트.");
        return "redirect:/login"; // 로그인 안 됨 -> 로그인 페이지로 리다이렉트
    }

    // System.out.println("세션에서 가져온 사용자 닉네임: " + user.getNickname());
    model.addAttribute("nickname", user.getNickname());

    return "user/mypage"; // mypage.html 출력
}
}