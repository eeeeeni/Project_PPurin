package com.ppurin.ppurin.user;

import org.springframework.stereotype.Service;

@Service
public class KakaoUserService {

    private final KakaoRepository userRepository;

    public KakaoUserService(KakaoRepository userRepository) {
        this.userRepository = userRepository;
    }

    public KakaoUserDTO findOrCreateUser(String nickname) {
        // 닉네임으로 회원 조회
        KakaoUserEntity user = userRepository.findByNickname(nickname);

        if (user == null) {
            // 새로운 회원 생성
            user = new KakaoUserEntity();
            user.setNickname(nickname); // 닉네임 설정
            // System.out.println("저장할 사용자 정보: " + user);
            userRepository.save(user);  // 사용자 저장
            // System.out.println("저장된 사용자 ID: " + user.getId());
        } else {
            // System.out.println("기존 사용자 정보: " + user);
        }

        // Entity → DTO 변환
        return new KakaoUserDTO(
            user.getId(),
            user.getNickname(),
            user.getRole().name()
        );
    }
}
