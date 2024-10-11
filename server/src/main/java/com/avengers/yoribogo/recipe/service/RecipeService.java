package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.dto.RecipeDTO;
import org.springframework.data.domain.Page;

public interface RecipeService {

    // 페이지 번호로 요리 레시피 목록 조회
    Page<RecipeDTO> findRecipeByPageNo(Integer pageNo);

}