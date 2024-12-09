package org.chefcrew.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorException {

    REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST.value(), "이미 가입된 이메일입니다"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "유저가 존재하지 않습니다."),
    PASSWORD_NOT_ACCORD(HttpStatus.BAD_REQUEST.value(), "비밀번호가 불일치합니다."),
    OPEN_API_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE.value(), "공공데이터 서버가 작동하지 않습니다.");
    int status;
    String errorMessage;

}
