package org.chefcrew.user.controller;

import lombok.RequiredArgsConstructor;
import org.chefcrew.user.dto.response.GetUserInfoResponse;
import org.chefcrew.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserInfoResponse> getUserInfo(@PathVariable("userId") long userId){
        return ResponseEntity.ok().body(userService.getUserInfo(userId));
    }
}
