package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.dto.RecipeDTO;
import org.springframework.data.domain.Page;

public interface RecipeService {

    // 페이지 번호로 요리 레시피 목록 조회
    Page<RecipeDTO> findRecipeByPageNo(Integer pageNo);

    // 요리 레시피 단건 조회
    RecipeDTO findRecipeByRecipeId(Long recipeId);

    // 요리 레시피 요리 이름으로 조회
    Page<RecipeDTO> findRecipeByMenuName(String menuName, Integer pageNo);

    RecipeDTO modifyRecipe(RecipeDTO modifyRecipeDTO);

}
