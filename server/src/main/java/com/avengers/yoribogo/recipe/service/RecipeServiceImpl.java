package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.recipe.domain.MenuType;
import com.avengers.yoribogo.recipe.domain.Recipe;
import com.avengers.yoribogo.recipe.dto.AIRecipeDTO;
import com.avengers.yoribogo.recipe.dto.PublicDataRecipeDTO;
import com.avengers.yoribogo.recipe.dto.RecipeDTO;
import com.avengers.yoribogo.recipe.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class RecipeServiceImpl implements RecipeService {

    private final Integer ELEMENTS_PER_PAGE = 10;

    private final ModelMapper modelMapper;
    private final RecipeRepository recipeRepository;
    private final PublicDataRecipeService publicDataRecipeService;
    private final AIRecipeService aiRecipeService;

    @Autowired
    public RecipeServiceImpl(ModelMapper modelMapper,
                             RecipeRepository recipeRepository,
                             PublicDataRecipeService publicDataRecipeService,
                             AIRecipeService aiRecipeService) {
        this.modelMapper = modelMapper;
        this.recipeRepository = recipeRepository;
        this.publicDataRecipeService = publicDataRecipeService;
        this.aiRecipeService = aiRecipeService;
    }

    // 페이지 번호로 요리 레시피 조회
    @Override
    public Page<RecipeDTO> findRecipeByPageNo(Integer pageNo) {
        // 페이지 번호 유효성 검사
        if (pageNo == null || pageNo < 1) {
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        }

        // 페이지와 한 페이지당 요소 수 설정 (pageNumber는 0부터 시작)
        Pageable pageable = PageRequest.of(
                pageNo - 1,
                ELEMENTS_PER_PAGE,
                Sort.by(Sort.Direction.DESC, "recipeId")
        );

        // 레시피 조회
        Page<Recipe> recipePage = recipeRepository.findAll(pageable);

        // 레시피가 존재하지 않는 경우
        if (recipePage.getContent().isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE);
        }

        // Recipe -> RecipeDTO 변환
        List<RecipeDTO> recipeDTOList = convertEntityPageToDTOPage(recipePage);

        // 새로운 Page 객체 생성
        return new PageImpl<>(recipeDTOList, pageable, recipePage.getTotalElements());
    }

    // 요리 레시피 단건 조회
    @Override
    public RecipeDTO findRecipeByRecipeId(Long recipeId) {
        // 레시피 조회
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE));
        return modelMapper.map(recipe, RecipeDTO.class);
    }

    // 요리 레시피 요리 이름으로 조회
    @Override
    public Page<RecipeDTO> findRecipeByMenuName(String menuName, Integer pageNo) {
        // 페이지 번호 유효성 검사
        if (pageNo == null || pageNo < 1) {
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        }

        // 페이지와 한 페이지당 요소 수 설정 (pageNumber는 0부터 시작)
        Pageable pageable = PageRequest.of(
                pageNo - 1,
                ELEMENTS_PER_PAGE,
                Sort.by(Sort.Direction.DESC, "recipeId")
        );

        // 레시피 조회
        Page<Recipe> recipePage = recipeRepository.findByMenuNameContaining(menuName, pageable);

        // 레시피가 존재하지 않는 경우
        if (recipePage.getContent().isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE);
        }

        // Recipe -> RecipeDTO 변환
        List<RecipeDTO> recipeDTOList = convertEntityPageToDTOPage(recipePage);

        // 새로운 Page 객체 생성
        return new PageImpl<>(recipeDTOList, pageable, recipePage.getTotalElements());
    }

    // 요리 레시피 등록
    @Override
    public RecipeDTO registRecipe(RecipeDTO registRecipeDTO) {
        // 요리 레시피 테이블에 저장
        Recipe newRecipe = modelMapper.map(registRecipeDTO, Recipe.class);
        newRecipe = recipeRepository.save(newRecipe);

        // 요리 구분 검사
        if (registRecipeDTO.getMenuType() == MenuType.PUBLIC) {
            // DTO에 요리 레시피 정보 담기
            PublicDataRecipeDTO publicDataRecipeDTO = PublicDataRecipeDTO
                    .builder()
                    .publicDataMenuName(newRecipe.getMenuName())
                    .publicDataMenuIngredient(newRecipe.getMenuIngredient())
                    .publicDataMenuImage(newRecipe.getMenuImage())
                    .recipeId(newRecipe.getRecipeId())
                    .build();

            // 공공데이터 요리 레시피 등록
            publicDataRecipeService.registPublicDataRecipe(publicDataRecipeDTO);
        } else if (registRecipeDTO.getMenuType() == MenuType.AI) {
            // DTO에 요리 레시피 정보 담기
            AIRecipeDTO aiRecipeDTO = AIRecipeDTO
                    .builder()
                    .aiMenuName(newRecipe.getMenuName())
                    .aiMenuIngredient(newRecipe.getMenuIngredient())
                    .recipeId(newRecipe.getRecipeId())
                    .build();

            // AI 요리 레시피 등록
            aiRecipeService.registAIRecipe(aiRecipeDTO);
        } else {
            // 요리 구분이 잘못되었을 경우
            throw new CommonException(ErrorCode.INVALID_REQUEST_BODY);
        }

        return modelMapper.map(newRecipe, RecipeDTO.class);
    }

    // 요리 레시피 수정
    @Override
    public RecipeDTO modifyRecipe(Long recipeId, RecipeDTO modifyRecipeDTO) {
        // 기존 엔티티 조회
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE));

        // 엔티티 정보 수정
        existingRecipe.setMenuName(modifyRecipeDTO.getMenuName());
        existingRecipe.setMenuImage(modifyRecipeDTO.getMenuImage());
        existingRecipe.setMenuIngredient(modifyRecipeDTO.getMenuIngredient());
        existingRecipe.setUserId(modifyRecipeDTO.getUserId());

        return modelMapper.map(recipeRepository.save(existingRecipe), RecipeDTO.class);
    }

    // 요리 레시피 삭제
    @Override
    public void removeRecipe(Long recipeId) {
        // 기존 엔티티 조회
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE));

        recipeRepository.delete(existingRecipe);
    }

    // 페이지 내 엔티티를 DTO로 변환해주는 메소드
    private List<RecipeDTO> convertEntityPageToDTOPage(Page<Recipe> recipePage) {
        return recipePage.getContent().stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDTO.class))
                .toList();
    }

}
