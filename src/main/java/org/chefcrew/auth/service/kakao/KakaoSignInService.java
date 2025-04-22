package org.chefcrew.auth.service.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.exception.CustomException;
import org.chefcrew.common.exception.ErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String redirectUri = selectRedirectUri(domainName);
        body.add("grant_type", "authorization_code");
        body.add("client_id", KAKAO_CLIENT_ID);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

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
        return jsonNode.get("access_token").asText(); //토큰 전송
    }

    public LoginResult getKaKaoUserData(String accessToken) {
        ResponseEntity<Object> responseData = requestKakaoServer(accessToken, Strategy.LOGIN);
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap profileResponse = (HashMap) objectMapper.convertValue(responseData.getBody(), Map.class)
                .get("properties");
        return LoginResult.of(objectMapper.convertValue(responseData.getBody(), Map.class).get("id").toString(),
                profileResponse == null || profileResponse.get("profile_image") == null ? null
                        : profileResponse.get("profile_image").toString(),
                profileResponse == null || profileResponse.get("nickname") == null ? null
                        : profileResponse.get("nickname").toString()); //프로필 이미지 허용 x시 null값으로 넘김
    }

    public String withdrawKakao(String socialId) {
        ResponseEntity<Object> responseData = requestKakaoServer(socialId, Strategy.WITHDRAWAL);
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap profileResponse = (HashMap) objectMapper.convertValue(responseData.getBody(), Map.class);
        return profileResponse.get("id").toString();
    }

    private ResponseEntity<Object> requestKakaoServer(String idOrAccessToken, Strategy strategy) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        switch (strategy) {
            case WITHDRAWAL -> {
                headers.add("Authorization", "KakaoAK " + KAKAO_AK);

                MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
                param.set("target_id_type", "user_id");
                param.set("target_id", idOrAccessToken);
                HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);

                return restTemplate.postForEntity(KAKAO_WITHDRAW, httpEntity, Object.class);
            }
            case LOGIN -> {
                headers.add("Authorization", "Bearer " + idOrAccessToken);

                HttpEntity<JsonArray> httpEntity = new HttpEntity<>(headers);
                return restTemplate.postForEntity(KAKAO_URL, httpEntity, Object.class);
            }
        }
        throw new CustomException(ErrorException.UNPROCESSABLE_KAKAO_SERVER_EXCEPTION);
    }

}
