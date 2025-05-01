package org.chefcrew.auth.dto.response;

public record SignInResponse(Long userId, String accessToken, String refreshToken, Boolean isRegistered,
                             String profile) {
    public static SignInResponse of(Long userId, String accessToken, String refreshToken,
                                    Boolean isRegistered, String profile) {
        return new SignInResponse(userId, accessToken, refreshToken, isRegistered, profile);
    }
}
