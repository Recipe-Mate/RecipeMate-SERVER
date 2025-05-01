package org.chefcrew.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.response.ErrorResponse;
import org.chefcrew.jwt.UserId;
import org.chefcrew.user.dto.response.GetUserInfoResponse;
import org.chefcrew.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Controller", description = "유저 정보 조회 관련 API")
@SecurityRequirement(name = "jwt-cookie")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    @Operation(summary = "유저 정보 조회", description = "현재 로그인된 유저의 정보를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "JWT 인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping
    public ResponseEntity<GetUserInfoResponse> getUserInfo(
            @UserId Long userId
    ) {
        return ResponseEntity.ok().body(userService.getUserInfo(userId));
    }

/*    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest requestBody) {
        userService.addUser(requestBody);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody SignUpRequest requestBody){
        return ResponseEntity.ok(userService.login(requestBody));
    }*/
}
