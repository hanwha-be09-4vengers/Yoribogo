package com.avengers.yoribogo.user.dto;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_auth_id", length = 255)
    private String userAuthId;

    @Column(name = "user_name", length = 255)
    private String userName;

    @Column(length = 255, nullable = true)
    private String password;

    @Column(length = 255, nullable = true)
    private String nickname;

    @Column(length = 255)
    private String email;

    @Column(name = "user_status", length = 255)
    private String userStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "withdrawn_at", nullable = true)
    private LocalDateTime withdrawnAt;

    @Column(name = "profile_image", columnDefinition = "TEXT", nullable = true)
    private String profileImage;

    @Column(name = "signup_path", length = 255)
    private String signupPath;

    @Column(name = "user_role", length = 255)
    private String userRole;

    @Column(name = "user_identifier", length = 511)
    private String userIdentifier;

    @Column(name = "user_likes")
    private Long userLikes;

    @Column(name = "tier_id", nullable = true)
    private Integer tierId;

    @Column(name = "accept_status", length = 255)
    private String acceptStatus;
}