package com.avengers.yoribogo.user.dto.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserProfileDTO {
    private String nickname;
    private String profileImage;
    private String tierName;
    private String email;
    private String userRole;
    private String signupPath;
}