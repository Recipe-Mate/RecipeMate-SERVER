package org.chefcrew.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chefcrew.auth.dto.response.SignInResponse;
import org.chefcrew.auth.dto.response.TokenHealthDto;
import org.chefcrew.auth.dto.response.TokenResponse;
import org.chefcrew.auth.service.kakao.KakaoSignInService;
import org.chefcrew.auth.service.kakao.LoginResult;
import org.chefcrew.common.exception.CustomException;
import org.chefcrew.common.exception.ErrorException;
import org.chefcrew.jwt.JwtService;
import org.chefcrew.food.service.FoodService;
import org.chefcrew.jwt.JwtValidationType;
import org.chefcrew.recipe.service.OwnRecipeService;
import org.chefcrew.user.entity.User;
import org.chefcrew.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.chefcrew.common.constants.JWTConstants.ACCESS_TOKEN;
import static org.chefcrew.common.constants.JWTConstants.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoSignInService kakaoSignInService;
    private final JwtService jwtService;
    private final FoodService foodService;

    private final UserRepository userRepository;
    private final OwnRecipeService ownRecipeService;

    private final Long TOKEN_EXPIRATION_TIME_ACCESS = 7 * 24 * 60 * 60 * 1000L; //7일
    private final Long TOKEN_EXPIRATION_TIME_REFRESH = 21 * 24 * 60 * 60 * 1000L; //21일
    @Value("${static-image.root}")
    private String BASIC_ROOT;

    @Value("${static-image.url}")
    private String BASIC_THUMBNAIL;

    @Transactional
    public SignInResponse signIn(String code, String domainName) throws IOException {
        LoginResult loginResult = login(code, domainName);
        String socialId = loginResult.id();
        String profileImage = loginResult.profile();
        String nickname = loginResult.nickname();
        String email = loginResult.email();

        if (!isAlreadySignedUser(socialId)) {
            User newUser = User.builder()
                    .nickname(nickname == null ? "새 유저" : nickname)
                    .socialId(socialId)
                    .profile(profileImage)
                    .email(email)
                    .build();
            userRepository.save(newUser);

        } else if (isAlreadySignedUser(socialId)) {
            userRepository.findBySocialId(socialId).ifPresent(user -> {
                if(isSignedUserButEmailNotSaved(socialId) || !user.getEmail().equals(email)){
                    user.updateEmail(email);
                }
                if(!user.getProfile().equals(profileImage)) {
                    user.updateProfile(profileImage);
                }

            });
        }

        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new CustomException(ErrorException.USER_NOT_FOUND));

        // 자체 jwt 발급 (서버 내 액세스 토큰/리프레시 토큰)
        String accessToken = jwtService.issuedToken(String.valueOf(user.getUserId()), TOKEN_EXPIRATION_TIME_ACCESS, ACCESS_TOKEN);
        String refreshToken = jwtService.issuedToken(String.valueOf(user.getUserId()), TOKEN_EXPIRATION_TIME_REFRESH, REFRESH_TOKEN);

        user.updateRefreshToken(refreshToken);

        return SignInResponse.of(user.getUserId(), accessToken, refreshToken, isAlreadySignedUser(socialId),
                user.getProfile());
    }

    private boolean isAlreadySignedUser(String socialId) {
        return userRepository.existsBySocialId(socialId);
    }

    // email 칼럼 추가로 인한 기존 db 보존하는 마이그레이션을 위한 메서드
    private boolean isSignedUserButEmailNotSaved(String userSocialId) {
        return isAlreadySignedUser(userSocialId) && userRepository.findBySocialId(userSocialId).get().getEmail() == null;
    }

    @Transactional
    public TokenResponse issueToken(String refreshToken) {
        jwtService.verifyToken(refreshToken);

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorException.USER_NOT_FOUND));

        // 자체 jwt 발급 (서버 내 액세스 토큰/리프레시 토큰)
        String newAccessToken = jwtService.issuedToken(String.valueOf(user.getUserId()), TOKEN_EXPIRATION_TIME_ACCESS, ACCESS_TOKEN);
        String newRefreshToken = jwtService.issuedToken(String.valueOf(user.getUserId()),
                TOKEN_EXPIRATION_TIME_REFRESH, REFRESH_TOKEN);

        user.updateRefreshToken(newRefreshToken);

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void signOut(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorException.USER_NOT_FOUND));
        user.updateRefreshToken(null);
    }

    private LoginResult login(String code, String domainName) {
        String socialAccessToken = kakaoSignInService.getAccessToken(code, domainName);
        return kakaoSignInService.getKaKaoUserData(socialAccessToken);
    }

    //탈퇴
    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(ErrorException.USER_NOT_FOUND));
        String deleteSocialId = kakaoSignInService.withdrawKakao(user.getSocialId());

        try {
            foodService.deleteAllByUser(user);
            ownRecipeService.deleteAllByUser(user);
        } catch (IOException e) {
            throw new CustomException(ErrorException.UNPROCESSABLE_ENTITY_DELETE_EXCEPTION);
        }

        Long res = userRepository.deleteByUserId(userId); //삭제한 row 갯수가 1개 일 때 정상/이외는 에러
        System.out.println(deleteSocialId + " 유저 삭제.");
        System.out.println(res + "개의 row 삭제.");
        if (res != 1) {
            throw new CustomException(ErrorException.UNPROCESSABLE_ENTITY_DELETE_EXCEPTION);
        }
    }

    @Transactional(readOnly = true)
    public TokenHealthDto checkHealthOfToken(String refreshToken) {
        return TokenHealthDto.of(
                jwtService.verifyToken(refreshToken) == JwtValidationType.VALID_REFRESH
        );
    }

}
