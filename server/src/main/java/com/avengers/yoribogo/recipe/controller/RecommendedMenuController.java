package com.avengers.yoribogo.recipe.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.recipe.dto.GoodMenuDTO;
import com.avengers.yoribogo.recipe.service.RecommendedMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommended-menus")
public class RecommendedMenuController {

    private final RecommendedMenuService recommendedMenuService;

    @Autowired
    public RecommendedMenuController(RecommendedMenuService recommendedMenuService) {
        this.recommendedMenuService = recommendedMenuService;
    }

    @GetMapping
    public ResponseDTO<List<GoodMenuDTO>> getRecommendedMenuByUserId(@RequestParam("user") Long userId) {
        List<GoodMenuDTO> recommendedMenuList = recommendedMenuService.findRecommendedMenuByUserId(userId);
        return ResponseDTO.ok(recommendedMenuList);
    }

}
