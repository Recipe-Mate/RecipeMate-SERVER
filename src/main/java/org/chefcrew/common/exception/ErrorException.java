package org.chefcrew.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorException {

    /**
     * 400 BAD REQUEST EXCEPTION
     */
    REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다."),
    RECIPE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "잘못된 레시피 정보입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST.value(), "이미 가입된 이메일입니다"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "유저가 존재하지 않습니다."),
    PASSWORD_NOT_ACCORD(HttpStatus.BAD_REQUEST.value(), "비밀번호가 불일치합니다."),
    INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST.value(), "지원하지 않는 이미지 확장자입니다."),
    /**
     * 401 UNAUTHORIZED EXCEPTION
     */
    TIME_EXPIRED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다."),
    WRONG_TYPE_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), "인증토큰이 존재하지 않습니다."),
    UNKNOWN_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), "토큰 형식이 유효하지 않습니다."),
    UNSUPPORTED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), "지원하지 않는 토큰 방식입니다."),
    WRONG_SIGNATURE_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), "Signature가 잘못된 토큰입니다."),
    UNEXPECTED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), "알 수 없는 토큰 예외입니다."),
    /**
     * 422 UNPROCESSABLE_ENTITY
     */
    UNPROCESSABLE_KAKAO_SERVER_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY.value(), "카카오서버와 통신 과정에서 오류가 발생했습니다."),
    UNPROCESSABLE_ENTITY_DELETE_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY.value(), "db에서 삭제 과정에서 오류가 발생했습니다."),
    /**
     * 503 SERVICE UNAVAILABLE
     */
    OPEN_API_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE.value(), "공공데이터 서버가 작동하지 않습니다.");
    int status;
    String errorMessage;

}
