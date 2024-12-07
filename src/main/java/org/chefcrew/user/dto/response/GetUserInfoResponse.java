package org.chefcrew.user.dto.response;

public record GetUserInfoResponse(
        long userId,
        String userName,
        String email
) {
}
