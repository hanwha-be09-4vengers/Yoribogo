package com.avengers.yoribogo.recipe.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.recipe.dto.RecipeDTO;
import com.avengers.yoribogo.recipe.service.RecipeServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final ModelMapper modelMapper;
    private final RecipeServiceImpl recipeService;

    @Autowired
    public RecipeController(ModelMapper modelMapper, RecipeServiceImpl recipeService) {
        this.modelMapper = modelMapper;
        this.recipeService = recipeService;
    }

    // 페이지 번호로 요리 레시피 목록 조회
    @GetMapping
    public ResponseDTO<Page<RecipeDTO>> getRecipeByPageNo(@RequestParam("page") Integer pageNo) {
        Page<RecipeDTO> recipes = recipeService.findRecipeByPageNo(pageNo);
        return ResponseDTO.ok(recipes);
    }

}
