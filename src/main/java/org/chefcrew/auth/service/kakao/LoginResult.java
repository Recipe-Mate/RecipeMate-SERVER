package org.chefcrew.auth.service.kakao;

public record LoginResult(String id, String profile, String nickname, String email) {
    public static LoginResult of(String id, String profile, String nickname, String email) {
        return new LoginResult(id, profile, nickname, email);
    }
}
