package org.chefcrew.auth.controller;

import java.io.IOException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.chefcrew.auth.dto.response.SignInResponse;
import org.chefcrew.auth.dto.response.TokenHealthDto;
import org.chefcrew.auth.dto.response.TokenResponse;
import org.chefcrew.auth.service.AuthService;
import org.chefcrew.common.response.ErrorResponse;
import org.chefcrew.jwt.UserId;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Controller", description = "유저 로그인, jwt 토큰 및 탈퇴 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "카카오 로그인", description = "인가 코드를 통해 카카오 로그인 처리 및 JWT 발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공 및 토큰 발급"),
            @ApiResponse(responseCode = "401", description = "인증토큰이 존재하지 않습니다",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SignInResponse> signIn(
            @Parameter(description = "카카오 인가 코드", required = true)
            @RequestParam("code") String code,
            HttpServletRequest request
    ) throws IOException {
        String currentDomain = request.getServerName();
        return ResponseEntity.ok(authService.signIn(code, currentDomain));
    }

    @Operation(summary = "리프레시 토큰으로 액세스 토큰 재발급", description = "리프레시 토큰을 통해 새로운 JWT 발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "jwt-cookie")
    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenResponse> reissueToken(
            @Parameter(description = "리프레시 토큰", required = true)
            @RequestHeader String refreshToken
    ) {
        return ResponseEntity.ok(authService.issueToken(refreshToken));
    }

    @PostMapping("/sign-out")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity signOut(
            @UserId Long userId
    ) {
        authService.signOut(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity withdraw(
            @UserId Long userId
    ) {
        authService.withdraw(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리프레시 토큰 유효성 확인", description = "JWT 리프레시 토큰이 유효한지 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 유효"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아님",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "jwt-cookie")
    @PostMapping("/token/health")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenHealthDto> checkHealthOfToken(
            @Parameter(description = "확인할 JWT 리프레시토큰", required = true)
            @RequestHeader String token
    ) {
        return ResponseEntity.ok(authService.checkHealthOfToken(token));
    }
}
