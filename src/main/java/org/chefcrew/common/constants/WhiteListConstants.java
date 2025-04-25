package org.chefcrew.common.constants;

import java.util.List;

public class WhiteListConstants {

    public static final List<String> FILTER_WHITE_LIST = List.of(
            "/auth",
            "/auth/token",
            "/auth/token/health",
            "/oauth/kakao/**",
            "/api/kakao/login",
            "/swagger-ui/",
            "/swagger-resources/",
            "/api-docs/"
    );

    public static final String[] SECURITY_WHITE_LIST = {
            "/auth",
            "/auth/token",
            "/auth/token/health",
            "/oauth/kakao/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/api-docs/**"
    };

}
