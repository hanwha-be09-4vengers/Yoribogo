package com.avengers.yoribogo.user.domain.vo.signup;

import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestResistAdminUserVO {
    @JsonProperty("user_auth_id")
    private String userAuthId; // 사용자가 직접 입력하는 ID, 일반 로그인 시 유니크해야 함

    @JsonProperty("user_name")
    private String userName; // 사용자의 실제 이름으로 필드 이름 수정

    @JsonProperty("password")
    private String password; // 암호화 되기 전 사용자가 입력한 평문(비밀번호)

    @JsonProperty("email")
    private String email; // 이메일은 선택 사항으로 변경

    @JsonProperty("signup_path")
    private SignupPath signupPath; // 가입 경로 (NORMAL, KAKAO, GOOGLE 등)

}