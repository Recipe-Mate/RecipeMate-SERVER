package org.chefcrew.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = true)
    private String refreshToken;

    @Column(nullable = true)
    private String profile;

    @Column(nullable = true)
    private String email;

    @Builder
    public User(String nickname, String socialId, String refreshToken, String profile, String email) {
        this.nickname = nickname;
        this.socialId = socialId;
        this.refreshToken = refreshToken;
        this.profile = profile;
        this.email = email;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfile(String profile) {
        this.profile = profile;
    }
}
