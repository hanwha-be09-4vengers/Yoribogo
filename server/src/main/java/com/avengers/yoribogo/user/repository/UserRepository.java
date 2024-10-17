package com.avengers.yoribogo.user.repository;

import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.dto.EnterpriseUserDTO;
import com.avengers.yoribogo.user.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    @Query("SELECT COALESCE(MAX(u.userId), 0) FROM UserEntity u")
    Long findMaxUserId();

    Optional<UserEntity> findByUserIdentifier(String userIdentifier);

    Optional<UserEntity> findByNickname(String nickname);

    // 이름, 가입 경로, 이메일로 사용자 찾기
    Optional<UserEntity> findByNicknameAndSignupPathAndEmail(String userName, SignupPath signupPath, String email);

    // 아이디와 이메일로 사용자 찾기
    Optional<UserEntity> findByUserAuthIdAndEmail(String userAuthId, String email);

    // userAuthId로 사용자 조회
    Optional<UserEntity> findByUserAuthId(String userAuthId);

    // userId로 사용자 조회
    Optional<UserEntity> findByUserId(Long userId);
}
