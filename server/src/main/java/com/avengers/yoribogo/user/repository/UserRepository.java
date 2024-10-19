package com.avengers.yoribogo.user.repository;

import com.avengers.yoribogo.user.dto.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 알림 기능 위해 추가
    Optional<UserEntity> findById(Long id);  // 특정 회원을 Optional로 조회
}


