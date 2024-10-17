package com.avengers.yoribogo.recipeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
