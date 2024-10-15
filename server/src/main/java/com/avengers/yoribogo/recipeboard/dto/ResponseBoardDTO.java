package com.avengers.yoribogo.recipeboard.dto;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseBoardDTO {

    private Long recipeBoardId;
    private String recipeBoardMenuName;
    private String recipeBoardIngredient;
    private String recipeBoardImage;
    private LocalDateTime recipeBoardCreatedAt;
    private int recipeBoardLikes;
    private int recipeBoardComments;
    private RecipeBoardStatus recipeBoardStatus;
    private Long userId;

    private List<RecipeBoardManualDTO> manuals;
}
