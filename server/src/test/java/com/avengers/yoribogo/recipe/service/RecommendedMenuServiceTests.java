package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.recipe.dto.GoodMenuDTO;
import com.avengers.yoribogo.recipe.dto.RecommendedMenuDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
class RecommendedMenuServiceTests {

    @Autowired
    private RecommendedMenuService recommendedMenuService;

    @DisplayName("추천 요리 회원별 조회 테스트")
    @Test
    void testFindRecommendedMenuByUserId() {
        // Given
        Long userId = 3L;

        // When
        List<GoodMenuDTO> recommendedMenuDTOList = recommendedMenuService.findRecommendedMenuByUserId(userId);

        // Then
        Assertions.assertNotNull(recommendedMenuDTOList, "추천 요리가 null 입니다");

        // 로그 찍기
        for (GoodMenuDTO goodMenuDTO : recommendedMenuDTOList) {
            log.info(goodMenuDTO.toString());
        }
    }

    @DisplayName("추천 요리 삭제 테스트")
    @Test
    void testRemoveRecommendedMenu() {
        // Given
        Long recommendedMenuId = 1L;

        // When
        recommendedMenuService.removeRecommendedMenu(recommendedMenuId);

        // Then
        Assertions.assertThrows(CommonException.class,
                () -> recommendedMenuService.removeRecommendedMenu(recommendedMenuId),
                "추천 요리가 삭제되지 않았습니다."
        );
    }

}
