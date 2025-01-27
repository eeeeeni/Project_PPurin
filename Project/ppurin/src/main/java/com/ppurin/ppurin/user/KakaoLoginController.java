package com.ppurin.ppurin.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final KakaoUserService kakaoUserService; // KakaoUserService 추가

    public KakaoLoginController(KakaoLoginService kakaoLoginService, KakaoUserService kakaoUserService) {
        this.kakaoLoginService = kakaoLoginService;
        this.kakaoUserService = kakaoUserService; // 주입
    }

    // 카카오 로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model) {
        // 카카오 로그인 URL 생성
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=fb5c1974092e79b0f62adcefcb016818"
                + "&redirect_uri=https://ppurin.eeeni.store/oauth2/authorization/kakao"
                + "&response_type=code";
        model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);
        return "user/login"; // login.html 출력
    }

    // 카카오 인증 후 콜백 처리
    @GetMapping("/oauth2/authorization/kakao")
    public String kakaoCallback(@RequestParam String code, HttpSession session, Model model) {
        try {
            // 닉네임 가져오기
            String nickname = kakaoLoginService.getUserNickname(code);
            // System.out.println("카카오로부터 가져온 닉네임: " + nickname);

            // 사용자 저장 또는 조회
            KakaoUserDTO user = kakaoUserService.findOrCreateUser(nickname);
            // System.out.println("DB에 저장된 사용자 정보: " + user);

             // 사용자 role을 확인하여 ADMIN이면 isAdmin 값을 세션에 저장
                if (user.getRole().equals("ADMIN")) {
                    session.setAttribute("isAdmin", true); // 관리자는 true로 설정
                } else {
                    session.setAttribute("isAdmin", false); // 일반 사용자는 false로 설정
                }

            // 세션에 사용자 정보 저장
            session.setAttribute("user", user);
            session.setAttribute("nickname", user.getNickname()); // 닉네임만 따로 저장

            // isAdmin을 model에 추가하여 Thymeleaf에서 사용할 수 있도록 전달
            model.addAttribute("isAdmin", session.getAttribute("isAdmin"));
            // System.out.println("Is Admin: " + session.getAttribute("isAdmin"));

            return "redirect:/index"; // 홈 페이지로 리다이렉트
        } catch (Exception e) {
            // 예외 처리 및 디버깅
            System.err.println("카카오 로그인 중 오류 발생: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error"; // 오류 페이지 출력
        }
    }
}

