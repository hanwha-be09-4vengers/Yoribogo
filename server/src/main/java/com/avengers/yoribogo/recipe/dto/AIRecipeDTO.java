package com.avengers.yoribogo.recipe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AIRecipeDTO {

    @JsonProperty("ai_recipe_id")
    private Long aiRecipeId;

    @JsonProperty("menu_name")
    private String aiMenuName;

    @JsonProperty("menu_ingredient")
    private String aiMenuIngredient;

    @JsonProperty("recipe_id")
    private Long recipeId;

}
