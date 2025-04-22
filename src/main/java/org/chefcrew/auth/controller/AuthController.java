package org.chefcrew.auth.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.chefcrew.auth.dto.response.SignInResponse;
import org.chefcrew.auth.dto.response.TokenHealthDto;
import org.chefcrew.auth.dto.response.TokenResponse;
import org.chefcrew.auth.service.AuthService;
import org.chefcrew.jwt.UserId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SignInResponse> signIn(
            @RequestParam("code") String code,
            HttpServletRequest request
    ) throws IOException {
        String currentDomain = request.getServerName();
        return ResponseEntity.ok(authService.signIn(code, currentDomain));
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenResponse> reissueToken(
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

    @PostMapping("/token/health")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenHealthDto> checkHealthOfToken(
            @RequestHeader String token
    ) {
        return ResponseEntity.ok(authService.checkHealthOfToken(token));
    }
}
