// package com.ppurin.ppurin.user;

// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.http.*;
// import org.json.JSONObject;

// @Service
// public class KakaoService {

//     private final String CLIENT_ID = "fb5c1974092e79b0f62adcefcb016818"; // 카카오 REST API 키
//     private final String REDIRECT_URI = "http://localhost:8080/oauth/callback/kakao";

//     public String getKakaoAccessToken(String code) {
//         RestTemplate restTemplate = new RestTemplate();

//         String tokenUrl = "https://kauth.kakao.com/oauth/token";
//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

//         String body = "grant_type=authorization_code"
//                 + "&client_id=" + CLIENT_ID
//                 + "&redirect_uri=" + REDIRECT_URI
//                 + "&code=" + code;

//         HttpEntity<String> entity = new HttpEntity<>(body, headers);

//         ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, entity, String.class);

//         JSONObject json = new JSONObject(response.getBody());
//         return json.getString("access_token");
//     }

//     public KakaoUserDTO getKakaoUserInfo(String accessToken) {
//         RestTemplate restTemplate = new RestTemplate();

//         String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(accessToken);

//         HttpEntity<Void> entity = new HttpEntity<>(headers);
//         ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);

//         JSONObject json = new JSONObject(response.getBody());
//         KakaoUserDTO user = new KakaoUserDTO();
//         user.setId(json.getLong("id"));
//         user.setNickname(json.getJSONObject("properties").getString("nickname"));
//         user.setEmail(json.getJSONObject("kakao_account").optString("email", null));

//         return user;
//     }
// }