package com.avengers.yoribogo.recipeboard.dto;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    // 매뉴얼 리스트 dto에만 추가
    @JsonProperty("manuals")
    private List<RecipeBoardManualDTO> manuals;
}
