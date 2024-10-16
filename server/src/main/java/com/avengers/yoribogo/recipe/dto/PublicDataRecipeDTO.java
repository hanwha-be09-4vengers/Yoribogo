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
public class PublicDataRecipeDTO {

    @JsonProperty("public_data_recipe_id")
    private Long publicDataRecipeId;

    @JsonProperty("menu_name")
    private String publicDataMenuName;

    @JsonProperty("menu_ingredient")
    private String publicDataMenuIngredient;

    @JsonProperty("menu_image")
    private String publicDataMenuImage;

    @JsonProperty("recipe_id")
    private Long recipeId;

}
