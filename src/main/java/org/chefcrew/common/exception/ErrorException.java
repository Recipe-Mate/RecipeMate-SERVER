package org.chefcrew.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorException {

    REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.");
    int status;
    String errorMessage;

}
