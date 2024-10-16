package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.recipe.domain.RecommendedMenu;
import com.avengers.yoribogo.recipe.domain.RecommendedMenuStatus;
import com.avengers.yoribogo.recipe.domain.Satisfaction;
import com.avengers.yoribogo.recipe.dto.GoodMenuDTO;
import com.avengers.yoribogo.recipe.dto.RecommendedMenuDTO;
import com.avengers.yoribogo.recipe.repository.RecommendedMenuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendedMenuServiceImpl implements RecommendedMenuService {

    private final ModelMapper modelMapper;
    private final RecommendedMenuRepository recommendedMenuRepository;

    @Autowired
    public RecommendedMenuServiceImpl(ModelMapper modelMapper,
                                      RecommendedMenuRepository recommendedMenuRepository) {
        this.modelMapper = modelMapper;
        this.recommendedMenuRepository = recommendedMenuRepository;
    }

    // 추천 요리 회원별 조회
    @Override
    public List<GoodMenuDTO> findRecommendedMenuByUserId(Long userId) {
        // 기존 엔티티 목록 조회
        List<GoodMenuDTO> recommendedMenuList = recommendedMenuRepository.findRecommendedMenuWithRecipeByUserId(
                userId, Satisfaction.GOOD, RecommendedMenuStatus.ACTIVE);

        // 조회된 결과가 없을 때 예외 처리
        if (recommendedMenuList.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECOMMENDED_MENU);
        }

        return recommendedMenuList;
    }

}