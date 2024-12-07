package org.chefcrew.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
}
