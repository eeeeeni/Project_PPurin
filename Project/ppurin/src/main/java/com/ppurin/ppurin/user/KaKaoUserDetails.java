// package com.ppurin.ppurin.user;

// import java.util.Map;

// public class KakaoUserDetails {

//     private Map<String, Object> attributes;

//     public KakaoUserDetails(Map<String, Object> attributes) {
//         this.attributes = attributes;
//     }

//     public String getProvider() {
//         return "kakao";
//     }

//     public String getProviderId() {
//         return attributes.get("id").toString();
//     }

//     public String getEmail() {
//         return (String) ((Map<?, ?>) attributes.get("kakao_account")).get("email");
//     }

//     public String getNickname() {
//         return (String) ((Map<?, ?>) attributes.get("properties")).get("nickname");
//     }
// }