package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.dto.GoodMenuDTO;
import com.avengers.yoribogo.recipe.dto.RecommendedMenuDTO;

import java.util.List;

public interface RecommendedMenuService {

    List<GoodMenuDTO> findRecommendedMenuByUserId(Long userId);

}
