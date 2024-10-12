package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.openai.service.OpenAIService;
import com.avengers.yoribogo.recipe.domain.MenuType;
import com.avengers.yoribogo.recipe.domain.Recipe;
import com.avengers.yoribogo.recipe.dto.*;
import com.avengers.yoribogo.recipe.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class RecipeServiceImpl implements RecipeService {

    private final Integer ELEMENTS_PER_PAGE = 10;

    private final ModelMapper modelMapper;
    private final RecipeRepository recipeRepository;
    private final PublicDataRecipeService publicDataRecipeService;
    private final AIRecipeService aiRecipeService;
    private final OpenAIService openAIService;

    @Autowired
    public RecipeServiceImpl(ModelMapper modelMapper,
                             RecipeRepository recipeRepository,
                             PublicDataRecipeService publicDataRecipeService,
                             AIRecipeService aiRecipeService,
                             OpenAIService openAIService) {
        this.modelMapper = modelMapper;
        this.recipeRepository = recipeRepository;
        this.publicDataRecipeService = publicDataRecipeService;
        this.aiRecipeService = aiRecipeService;
        this.openAIService = openAIService;
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

        // Recipe -> RecipeDTO 변환 및 Page 반환
        return convertEntityPageToDTOPage(recipePage);
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

        // Recipe -> RecipeDTO 변환 및 Page 반환
        return convertEntityPageToDTOPage(recipePage);
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
                    .menuName(newRecipe.getMenuName())
                    .menuIngredient(newRecipe.getMenuIngredient())
                    .menuImage(newRecipe.getMenuImage())
                    .recipeId(newRecipe.getRecipeId())
                    .build();

            // 공공데이터 요리 레시피 등록
            publicDataRecipeService.registPublicDataRecipe(publicDataRecipeDTO);
        } else if (registRecipeDTO.getMenuType() == MenuType.AI) {
            // DTO에 요리 레시피 정보 담기
            AIRecipeDTO aiRecipeDTO = AIRecipeDTO
                    .builder()
                    .menuName(newRecipe.getMenuName())
                    .menuIngredient(newRecipe.getMenuIngredient())
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
        existingRecipe.setMenuIngredient(modifyRecipeDTO.getMenuIngredient());
        existingRecipe.setMenuImage(modifyRecipeDTO.getMenuImage());
        existingRecipe.setUserId(modifyRecipeDTO.getUserId());

        // 요리 구분 검사
        if (existingRecipe.getMenuType() == MenuType.PUBLIC) {
            // DTO에 요리 레시피 정보 담기
            PublicDataRecipeDTO publicDataRecipeDTO = PublicDataRecipeDTO
                    .builder()
                    .menuName(modifyRecipeDTO.getMenuName())
                    .menuIngredient(modifyRecipeDTO.getMenuIngredient())
                    .menuImage(modifyRecipeDTO.getMenuImage())
                    .recipeId(existingRecipe.getRecipeId())
                    .build();

            // 공공데이터 요리 레시피 수정
            publicDataRecipeService.modifyPublicDataRecipe(publicDataRecipeDTO);
        } else if (existingRecipe.getMenuType() == MenuType.AI) {
            // DTO에 요리 레시피 정보 담기
            AIRecipeDTO aiRecipeDTO = AIRecipeDTO
                    .builder()
                    .menuName(modifyRecipeDTO.getMenuName())
                    .menuIngredient(modifyRecipeDTO.getMenuIngredient())
                    .recipeId(existingRecipe.getRecipeId())
                    .build();

            // AI 요리 레시피 수정
            aiRecipeService.modifyAIRecipe(aiRecipeDTO);
        } else {
            // 요리 구분이 잘못되었을 경우
            throw new CommonException(ErrorCode.INVALID_REQUEST_BODY);
        }

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

    // 요리 추천하기
    @Override
    public BaseRecipeDTO registRecommendRecipe(RequestRecommendDTO requestRecommendDTO) {
        // 1단계: AI에게 추천하는 요리 이름 물어보기
        String prompt = "다음 정보를 바탕으로 알맞은 요리를 하나만 추천해줘: " +
                "'오늘의 날씨는 어떤가요?' → '" + requestRecommendDTO.getFirst() + "', " +
                "'오늘의 기분은 어떤가요?' → '" + requestRecommendDTO.getSecond() + "', " +
                "'몇 명이 먹는 음식인가요?' → '" + requestRecommendDTO.getThird() + "', " +
                "'채식 또는 비건 식단을 따르시나요?' → '" + requestRecommendDTO.getFourth() + "', " +
                "'제가 또 알아야 하는게 있나요?' → '" + requestRecommendDTO.getFifth() + "'." +
                " 특히 '" + requestRecommendDTO.getFifth() + "'를 가장 중요하게 고려하고, " +
                "요리 이름은 특수문자와 다른 말은 빼고 오직 이름만 알려줘.";
        String aiAnswerMenu = openAIService.getRecommend(prompt).getChoices().get(0).getMessage().getContent();

        System.out.println(aiAnswerMenu);

        // 2단계: 공공데이터 요리 레시피 테이블 조회하기
        PublicDataRecipeDTO publicDataRecipeDTO = publicDataRecipeService.findPublicDataRecipeByMenuName(aiAnswerMenu);

        // 조회되었을 경우
        if (publicDataRecipeDTO != null) return publicDataRecipeDTO;

        // 4단계: AI 요리 레시피 테이블 조회하기
        AIRecipeDTO aiRecipeDTO = aiRecipeService.findAIRecipeByMenuName(aiAnswerMenu);

        // 조회되었을 경우
        if (aiRecipeDTO != null) return aiRecipeDTO;

        // 5단계: AI가 추천한 요리의 재료를 물어보기
        String ingredientsPrompt = "다음 요리를 만들 때 필요한 재료를 자세하게 설명해줘: " + aiAnswerMenu +
                "를 만들기 위한 재료를 꼭 ','로 구분하여 양을 포함하고, '설탕 2스푼' 형식으로 작성해줘. " +
                "단, 앞과 뒤에 특수문자와 다른 말은 빼고 오직 재료 내용만 제공해줘.";
        String aiAnswerIngredients = openAIService.getRecommend(ingredientsPrompt).getChoices().get(0).getMessage().getContent();

        // ':'가 있는 경우, ':' 이후의 문자열만 남기기
        aiAnswerIngredients = parseString(aiAnswerIngredients);

        // 6단계: AI가 추천한 요리의 레시피를 물어보기
        String recipePrompt = aiAnswerMenu + "를 만들기 위한 재료가 " + aiAnswerIngredients +
                "일 때, " + aiAnswerMenu + "의 레시피를 단계별로 자세하게 설명해줘:\n" +
                "레시피는 각 단계에 번호를 붙여서 '1. 파를 썹니다.'와 같은 형식으로 작성해줘. " +
                "단, 한자 사용을 피하고, 앞과 뒤에 특수문자와 다른 말은 빼고 오직 레시피 내용만 제공해줘.";
        String aiAnswerRecipe = openAIService.getRecommend(recipePrompt).getChoices().get(0).getMessage().getContent();

        // ':'가 있는 경우, ':' 이후의 문자열만 남기기
        aiAnswerRecipe = parseString(aiAnswerRecipe);

        // 7단계: AI가 생성한 요리 등록

        // AI가 생성한 요리 정보 입력
        RecipeDTO newRecipeDTO = RecipeDTO
                .builder()
                .menuName(aiAnswerMenu)
                .menuIngredient(aiAnswerIngredients)
                .menuType(MenuType.AI)
                .userId(1L)
                .build();

        // 8단계: AI가 생성한 요리 레시피 등록


        // 신규 등록 후 결과 반환
        return registRecipe(newRecipeDTO);
    }

    // ':'가 있는 경우, ':' 이후의 문자열만 남기는 메소드
    private static String parseString(String aiAnswer) {
        int colonIndex = aiAnswer.indexOf(":");
        if (colonIndex != -1) {
            aiAnswer = aiAnswer.substring(colonIndex + 1).trim();
        }
        return aiAnswer;
    }

    // Recipe -> RecipeDTO 변환 및 Page 반환 메소드
    private Page<RecipeDTO> convertEntityPageToDTOPage(Page<Recipe> recipePage) {
        List<RecipeDTO> recipeDTOList = recipePage.getContent().stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDTO.class))
                .toList();

        return new PageImpl<>(recipeDTOList, recipePage.getPageable(), recipePage.getTotalElements());
    }

}
