package com.avengers.yoribogo.recipe.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.recipe.dto.GoodMenuDTO;
import com.avengers.yoribogo.recipe.service.RecommendedMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommended-menus")
public class RecommendedMenuController {

    private final RecommendedMenuService recommendedMenuService;

    @Autowired
    public RecommendedMenuController(RecommendedMenuService recommendedMenuService) {
        this.recommendedMenuService = recommendedMenuService;
    }

    // 추천 요리 회원별 조회
    @GetMapping
    public ResponseDTO<?> getRecommendedMenuByUserId(@RequestParam("user") Long userId) {
        List<GoodMenuDTO> recommendedMenuList = recommendedMenuService.findRecommendedMenuByUserId(userId);
        return ResponseDTO.ok(recommendedMenuList);
    }

    // 추천 요리 삭제
    @DeleteMapping("/{recommendedMenuId}")
    public ResponseDTO<?> deleteRecommendedMenu(@PathVariable("recommendedMenuId") Long recommendedMenuId) {
        recommendedMenuService.removeRecommendedMenu(recommendedMenuId);
        return ResponseDTO.ok(null);
    }

}