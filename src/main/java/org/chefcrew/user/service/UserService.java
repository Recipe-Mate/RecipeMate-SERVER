package org.chefcrew.user.service;

import static org.chefcrew.common.exception.ErrorException.ALREADY_EXIST_EMAIL;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.exception.CustomException;
import org.chefcrew.user.dto.request.LoginRequest;
import org.chefcrew.user.dto.response.GetUserInfoResponse;
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
        return new GetUserInfoResponse(user.getUserId(), user.getUserName(), user.getEmail());
    }

    public void addUser(LoginRequest loginRequest){
        if(isDuplicateEmail(loginRequest.email()))
            throw new CustomException(ALREADY_EXIST_EMAIL);
        User user = new User(loginRequest.email(), loginRequest.password(), loginRequest.userName());
        userRepository.addUser(user);
    }

    public boolean isDuplicateEmail(String email){
        return userRepository.existByEmail(email);
    }
}
