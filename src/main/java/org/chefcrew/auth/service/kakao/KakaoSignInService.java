package org.chefcrew.auth.service.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.exception.CustomException;
import org.chefcrew.common.exception.ErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoSignInService {
    @Value("${jwt.KAKAO_URL}")
    private String KAKAO_URL;

    @Value("${jwt.KAKAO_WITHDRAW}")
    private String KAKAO_WITHDRAW;

    @Value("${jwt.KAKAO_AK}")
    private String KAKAO_AK;

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
