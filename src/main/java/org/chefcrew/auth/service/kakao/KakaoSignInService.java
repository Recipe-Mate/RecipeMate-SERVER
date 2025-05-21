package org.chefcrew.auth.service.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.constants.HTTPConstants;
import org.chefcrew.common.exception.CustomException;
import org.chefcrew.common.exception.ErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoSignInService {
    @Value("${jwt.KAKAO_URL}")
    private String KAKAO_URL;

    @Value("${jwt.KAKAO_WITHDRAW}")
    private String KAKAO_WITHDRAW;

    @Value("${jwt.KAKAO_AK}")
    private String KAKAO_AK;

    @Value("${jwt.KAKAO_CLIENT_ID}")
    private String KAKAO_CLIENT_ID;

    @Value("${jwt.KAKAO_REDIRECT_URI.LOCAL}")
    private String KAKAO_REDIRECT_URI_LOCAL;

    @Value("${jwt.KAKAO_REDIRECT_URI.PROD}")
    private String KAKAO_REDIRECT_URI_PROD;

    private String selectRedirectUri(String currentDomain) {
        if ("localhost".equals(currentDomain)) {
            return KAKAO_REDIRECT_URI_LOCAL;
        } else {
            return KAKAO_REDIRECT_URI_PROD;
        }
    }

    public String getAccessToken(String code, String domainName) {
        try {
            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add(HTTPConstants.CONTENT_TYPE, HTTPConstants.CONTENT_TYPE_FORM_URLENCODED);

            // HTTP Body 생성
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            String redirectUri = selectRedirectUri(domainName);
            log.error("리다이렉트 "+redirectUri);
            body.add(HTTPConstants.GRANT_TYPE, HTTPConstants.GRANT_TYPE_AUTHORIZATION_CODE);
            body.add(HTTPConstants.CLIENT_ID, KAKAO_CLIENT_ID);
            body.add(HTTPConstants.REDIRECT_URI, redirectUri);
            body.add(HTTPConstants.CODE, code);
            log.error("일단 통신직전까지는 됨 여기 들어옴");

            // HTTP 요청 보내기
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );

            // HTTP 응답 (JSON) -> 액세스 토큰 파싱
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = null;
            try {
                jsonNode = objectMapper.readTree(responseBody);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return jsonNode.get(HTTPConstants.ACCESS_TOKEN).asText(); //토큰 전송
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // 카카오 서버에서 발생한 400 오류 처리
                throw new CustomException(ErrorException.WRONG_TYPE_TOKEN_EXCEPTION);
            } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new CustomException(ErrorException.TIME_EXPIRED_TOKEN_EXCEPTION);
            } else {
                throw new CustomException(ErrorException.UNEXPECTED_TOKEN_EXCEPTION);
            }
        }

    }

    public LoginResult getKaKaoUserData(String accessToken) {
        ResponseEntity<Object> responseData = requestKakaoServer(accessToken, Strategy.LOGIN);
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap profileResponse = (HashMap) objectMapper.convertValue(responseData.getBody(), Map.class)
                .get(HTTPConstants.RESPONSE_PROPERTIES);
        HashMap kakaoAccountResponse = (HashMap) objectMapper.convertValue(responseData.getBody(), Map.class)
                .get(HTTPConstants.RESPONSE_KAKAO_ACCOUNT);
        log.error(kakaoAccountResponse.get(HTTPConstants.RESPONSE_EMAIL).toString());
        return LoginResult.of(objectMapper.convertValue(responseData.getBody(), Map.class).get("id").toString(),
                profileResponse == null || profileResponse.get(HTTPConstants.RESPONSE_PROFILE_IMAGE) == null ? null
                        : profileResponse.get(HTTPConstants.RESPONSE_PROFILE_IMAGE).toString(),
                profileResponse == null || profileResponse.get(HTTPConstants.RESPONSE_NICKNAME) == null ? null
                        : profileResponse.get(HTTPConstants.RESPONSE_NICKNAME).toString(),
                profileResponse == null || kakaoAccountResponse.get(HTTPConstants.RESPONSE_EMAIL) == null ? null
                        : kakaoAccountResponse.get(HTTPConstants.RESPONSE_EMAIL).toString()); //프로필 이미지 허용 x시 null값으로 넘김
    }

    public String withdrawKakao(String socialId) {
        ResponseEntity<Object> responseData = requestKakaoServer(socialId, Strategy.WITHDRAWAL);
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap profileResponse = (HashMap) objectMapper.convertValue(responseData.getBody(), Map.class);
        return profileResponse.get(HTTPConstants.RESPONSE_ID).toString();
    }

    private ResponseEntity<Object> requestKakaoServer(String idOrAccessToken, Strategy strategy) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        switch (strategy) {
            case WITHDRAWAL -> {
                headers.add(HTTPConstants.HEADER_AUTHORIZATION, HTTPConstants.HEADER_AUTHORIZATION_KAKAO + KAKAO_AK);

                MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
                param.set(HTTPConstants.PARAM_TARGET_ID_TYPE, HTTPConstants.PARAM_USER_ID);
                param.set(HTTPConstants.PARAM_TARGET_ID, idOrAccessToken);
                HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);

                return restTemplate.postForEntity(KAKAO_WITHDRAW, httpEntity, Object.class);
            }
            case LOGIN -> {
                headers.add(HTTPConstants.HEADER_AUTHORIZATION, HTTPConstants.HEADER_BEARER + idOrAccessToken);

                HttpEntity<JsonArray> httpEntity = new HttpEntity<>(headers);
                return restTemplate.postForEntity(KAKAO_URL, httpEntity, Object.class);
            }
        }
        throw new CustomException(ErrorException.UNPROCESSABLE_KAKAO_SERVER_EXCEPTION);
    }

}
