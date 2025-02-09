package org.chefcrew.user.service;

import static org.chefcrew.common.exception.ErrorException.ALREADY_EXIST_EMAIL;
import static org.chefcrew.common.exception.ErrorException.PASSWORD_NOT_ACCORD;
import static org.chefcrew.common.exception.ErrorException.USER_NOT_FOUND;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.exception.CustomException;
import org.chefcrew.user.dto.request.SignUpRequest;
import org.chefcrew.user.dto.response.GetUserInfoResponse;
import org.chefcrew.user.dto.response.LoginResponse;
import org.chefcrew.user.entity.User;
import org.chefcrew.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;

    public GetUserInfoResponse getUserInfo(long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        return new GetUserInfoResponse(user.getUserId(), user.getUserName(), user.getEmail());
    }

    @Transactional
    public void addUser(SignUpRequest signUpRequest) {
        if (isDuplicateEmail(signUpRequest.email())) {
            throw new CustomException(ALREADY_EXIST_EMAIL);
        }
        User user = new User(signUpRequest.email(), signUpRequest.password(), signUpRequest.userName());
        userRepository.addUser(user);
    }

    public boolean isDuplicateEmail(String email) {
        return userRepository.existByEmail(email);
    }

    public LoginResponse login(SignUpRequest signUpRequest) {
        User userInfo = userRepository.findByEmail(signUpRequest.email());
        System.out.println(signUpRequest);
        if (userInfo == null) {
            System.out.println("이메일 틀림");
            throw new CustomException(USER_NOT_FOUND);
        }
        if (userInfo.getPassword().equals(signUpRequest.password())) {
            System.out.println("로그인 성공");
            return new LoginResponse(userInfo.getUserId());
        } else {
            System.out.println("비번 틀림");
            throw new CustomException(PASSWORD_NOT_ACCORD);
        }
    }
}
