package com.ppurin.ppurin.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KakaoLoginService {

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.redirect.url}")
    private String redirectUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getUserNickname(String code) {
        try {
            // 1. Access Token 요청
            String accessToken = requestAccessToken(code);
            // System.out.println("Access Token: " + accessToken);

            // 2. 사용자 정보 요청
            return requestUserInfo(accessToken);

        } catch (Exception e) {
            throw new RuntimeException("카카오 로그인 중 오류 발생", e);
        }
    }

    // Access Token 요청
    private String requestAccessToken(String code) throws Exception {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> tokenRequest = new HttpEntity<>(
            "grant_type=authorization_code" +
            "&client_id=" + clientId +
            "&redirect_uri=" + redirectUrl +
            "&code=" + code +
            "&client_secret=" + clientSecret,
            headers
        );

        ResponseEntity<String> response = restTemplate.exchange(
            tokenUrl,
            HttpMethod.POST,
            tokenRequest,
            String.class
        );

        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    // 사용자 정보 요청
    private String requestUserInfo(String accessToken) throws Exception {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> userInfoRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            userInfoUrl,
            HttpMethod.GET,
            userInfoRequest,
            String.class
        );

        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        // System.out.println("사용자 정보 응답: " + jsonNode);

        // 사용자 닉네임 추출
        String nickname = jsonNode.path("kakao_account").path("profile").path("nickname").asText();
        // System.out.println("추출된 닉네임: " + nickname);
        return nickname;
    }
}

