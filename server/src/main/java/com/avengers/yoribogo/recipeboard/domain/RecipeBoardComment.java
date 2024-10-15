package com.avengers.yoribogo.recipeboard.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "RECIPE_BOARD_COMMENT")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class RecipeBoardComment {

    @Id
    @Column(name = "RECIPE_BOARD_COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeBoardCommentId;

    @Column(name = "RECIPE_BOARD_COMMENT_CONTENT")
    private String recipeBoardCommentContent;

    @Column(name = "RECIPE_BOARD_COMMENT_CREATED_AT")
    private LocalDateTime recipeBoardCommentCreatedAt;

    @Column(name = "RECIPE_BOARD_ID")
    private Long recipeBoardId;

    @Column(name = "USER_ID")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "RECIPE_BOARD_COMMENT_STATUS", nullable = false)
    private RecipeBoardCommentStatus recipeBoardCommentStatus = RecipeBoardCommentStatus.ACTIVE; // 기본값 설정

}

