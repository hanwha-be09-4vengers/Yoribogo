package com.avengers.yoribogo.RecipeBoard.dto;

import com.avengers.yoribogo.RecipeBoard.domain.RecipeBoardStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeBoardDTO {

    @JsonProperty("menu_name")
    private String recipeBoardMenuName;

    @JsonProperty("ingredients")
    private String recipeBoardIngredient;

    @JsonProperty("board_image")
    private String recipeBoardImage;

    @JsonProperty("likes")
    private int recipeBoardLikes;

    @JsonProperty("board_comments")
    private int recipeBoardComments;

    @JsonProperty("created_at")
    private LocalDateTime recipeBoardCreatedAt;

    @JsonProperty("board_status")
    private RecipeBoardStatus recipeBoardStatus;

    @JsonProperty("user_id")
    private Long userId;
}
