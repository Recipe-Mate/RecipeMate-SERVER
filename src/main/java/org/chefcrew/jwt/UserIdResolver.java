package org.chefcrew.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.constants.JWTConstants;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class UserIdResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class) && Long.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
                                  @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = request.getHeader("accessToken");
        Object tokenTypeObj = request.getAttribute(JWTConstants.TOKEN_TYPE);
        // 토큰 검증
        if (tokenTypeObj != JwtValidationType.VALID_ACCESS
                && tokenTypeObj != JwtValidationType.VALID_REFRESH) {
            throw new RuntimeException(
                    String.format("USER_ID를 가져오지 못했습니다. (%s - %s)", parameter.getClass(), parameter.getMethod()));
        }

        // 유저 아이디 반환
        String userIdObj = request.getAttribute(JWTConstants.USER_ID).toString();
        try {
            return Long.parseLong(userIdObj);
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    String.format("USER_ID를 가져오지 못했습니다. (%s - %s)", parameter.getClass(), parameter.getMethod()));
        }
    }
}
