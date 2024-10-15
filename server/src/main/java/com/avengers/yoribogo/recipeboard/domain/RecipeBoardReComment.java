package com.avengers.yoribogo.recipeboard.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "RECIPE_BOARD_RE_COMMENT")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class RecipeBoardReComment {

    @Id
    @ManyToOne
    @JoinColumn(name = "RECIPE_BOARD_COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private RecipeBoardComment recipeBoardReCommentId;

    @Column(name = "RECIPE_BOARD_RECOMMENT_CONTENT")
    private String recipeBoardReCommentContent;

    @Column(name = "RECIPE_BOARD_COMMENT_CREATED_AT")
    private LocalDateTime recipeBoardCommentCreatedAt;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "RECIPE_BOARD_COMMENT_ID")
    private Long recipeBoardCommentId;

    @Column(name = "RECIPE_BOARD_RECOMMENT_STATUS")
    private RecipeBoardReCommentStatus recipeBoardCommentStatus;



}
