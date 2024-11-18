package com.ppurin.ppurin.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.url}")
    private String redirectUrl;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    // 카카오 인증 후 콜백 처리
    @GetMapping("/oauth2/authorization/kakao")
    public String kakaoCallback(@RequestParam String code, Model model) {
        try {
            // 카카오 API로부터 사용자 닉네임 가져오기
            String nickname = kakaoLoginService.getUserNickname(code);
            model.addAttribute("nickname", nickname);
            return "index"; // 로그인 성공 후 이동할 페이지
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // 오류 발생 시 보여줄 페이지
        }
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model) {
        // 카카오 로그인 요청 URL 생성
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize"
            + "?client_id=" + clientId
            + "&redirect_uri=" + redirectUrl
            + "&response_type=code";

        model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);
        return "user/login"; // login.html 출력
    }
}