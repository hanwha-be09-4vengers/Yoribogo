package com.avengers.yoribogo.recipe.repository;

import com.avengers.yoribogo.recipe.domain.RecommendedMenu;
import com.avengers.yoribogo.recipe.domain.RecommendedMenuStatus;
import com.avengers.yoribogo.recipe.domain.Satisfaction;
import com.avengers.yoribogo.recipe.dto.GoodMenuDTO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecommendedMenuRepository extends JpaRepository<RecommendedMenu, Long> {

    // 추천 요리 회원별 조회
    @Query("SELECT new com.avengers.yoribogo.recipe.dto.GoodMenuDTO(rm, r) " +
            "FROM RecommendedMenu rm " +
            "JOIN Recipe r ON rm.recipeId = r.recipeId " +
            "WHERE rm.userId = :userId " +
            "AND rm.satisfaction = :satisfaction " +
            "AND rm.recommendedMenuStatus = :status")
    List<GoodMenuDTO> findRecommendedMenuWithRecipeByUserId(
            @Param("userId") Long userId,
            @Param("satisfaction") Satisfaction satisfaction,
            @Param("status") RecommendedMenuStatus status);

}
