package com.avengers.yoribogo.recipeboard.dto;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardCommentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class RecipeBoardCommentDTO {

    @JsonProperty("comment-id")
    private Long recipeBoardCommentId;

    @JsonProperty("comment-content")
    private String recipeBoardCommentContent;

    @JsonProperty("comment-created-at")
    private LocalDateTime recipeBoardCommentCreatedAt;

    @JsonProperty("recipe-board-id")
    private Long recipeBoardId;

    @JsonProperty("user-id")
    private Long userId;

    @JsonProperty("comment-status")
    private RecipeBoardCommentStatus recipeBoardCommentStatus;


}
