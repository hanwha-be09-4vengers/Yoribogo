package com.avengers.yoribogo.recipe.dto;

import com.avengers.yoribogo.recipe.domain.MenuType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RecipeDTO {

    @JsonProperty("recipe_id")
    private Long recipeId;

    @JsonProperty("menu_name")
    private String menuName;

    @JsonProperty("menu_ingredient")
    private String menuIngredient;

    @JsonProperty("menu_image")
    private String menuImage;

    @JsonProperty("menu_type")
    private MenuType menuType;

    @JsonProperty("user_id")
    private Long userId;

}
