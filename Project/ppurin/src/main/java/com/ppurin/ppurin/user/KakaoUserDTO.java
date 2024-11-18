package com.ppurin.ppurin.user;

public class KakaoUserDTO {

    private Long id;
    private String nickname;
    private String role;
    // private String password;  // 비밀번호 추가

    // Constructor
    public KakaoUserDTO(Long id, String nickname, String role) {
        this.id = id;
        this.nickname = nickname;
        this.role = role;
        // this.password = password;  // 비밀번호 설정
    }

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // public String getPassword() {
    //     return password;
    // }

    // public void setPassword(String password) {
    //     this.password = password;
    // }
}