package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.dto.PublicDataRecipeDTO;

public interface PublicDataRecipeService {

    // 공공데이터 요리 레시피 등록
    PublicDataRecipeDTO registPublicDataRecipe(PublicDataRecipeDTO publicDataRecipeDTO);

}
