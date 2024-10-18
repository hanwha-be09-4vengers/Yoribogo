package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoard;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardManual;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardStatus;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardDTO;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardManualDTO;
import com.avengers.yoribogo.recipeboard.dto.ResponseBoardDTO;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardManualRepository;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardRepository;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class RecipeBoardServiceImpl implements RecipeBoardService {

    public final Integer ELEMENTS_PER_PAGE = 12;

    private final ModelMapper modelMapper;
    private final RecipeBoardRepository recipeBoardRepository;
    private final RecipeBoardManualRepository recipeBoardManualRepository;

    @Autowired
    public RecipeBoardServiceImpl(ModelMapper modelMapper,
                                  RecipeBoardRepository recipeBoardRepository,
                                  RecipeBoardManualRepository recipeBoardManualRepository) {
        this.modelMapper = modelMapper;
        this.recipeBoardRepository = recipeBoardRepository;
        this.recipeBoardManualRepository = recipeBoardManualRepository;
    }

    @Override
    public ResponseBoardDTO registRecipeBoard(RecipeBoardDTO registRecipeBoardDTO) {
        // 1. RecipeBoard 게시글 저장 준비
        RecipeBoard newRecipeBoard = modelMapper.map(registRecipeBoardDTO, RecipeBoard.class);
        newRecipeBoard.setUserId(registRecipeBoardDTO.getUserId());  // User ID 설정

        // 서버에서 자동으로 설정할 필드
        newRecipeBoard.setRecipeBoardCreatedAt(LocalDateTime.now().withNano(0));
        newRecipeBoard.setRecipeBoardComments(0);
        newRecipeBoard.setRecipeBoardLikes(0);
        newRecipeBoard.setRecipeBoardStatus(RecipeBoardStatus.ACTIVE);

        // 2. RecipeBoard를 저장 (DB에 반영)
        newRecipeBoard = recipeBoardRepository.save(newRecipeBoard);  // 저장 후, 새로 저장된 객체 반환

        System.out.println("Saved RecipeBoard ID: " + newRecipeBoard.getRecipeBoardId());

        // 3. 저장된 RecipeBoard 객체를 ResponseBoardDTO로 변환하여 반환
        return modelMapper.map(newRecipeBoard, ResponseBoardDTO.class);
    }

    @Override
    public ResponseBoardDTO registRecipeBoardManual(Long recipeBoardId, List<RecipeBoardManualDTO> registRecipeBoardManualDTOs) {
        // 1. 보드가 존재하는지 확인
        RecipeBoard recipeBoard = recipeBoardRepository.findById(recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        // 2. 매뉴얼 리스트 처리 및 저장
        List<RecipeBoardManual> manualList = new ArrayList<>();
        for (RecipeBoardManualDTO manualDTO : registRecipeBoardManualDTOs) {
            RecipeBoardManual newManual = modelMapper.map(manualDTO, RecipeBoardManual.class);
            newManual.setRecipeBoard(recipeBoard);  // 보드와 매뉴얼을 연결
            manualList.add(newManual);
        }

        // 3. 매뉴얼 리스트 저장
        recipeBoardManualRepository.saveAll(manualList);

        // 4. 등록된 보드 정보 반환
        return modelMapper.map(recipeBoard, ResponseBoardDTO.class);
    }

    @Override
    public Page<RecipeBoardDTO> findRecipeBoardByPageNo(Integer pageNo) {

        // 페이지 번호 유효성 검사
        if (pageNo == null || pageNo < 1) {
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        }

        Pageable pageable = PageRequest.of(
                pageNo - 1,
                ELEMENTS_PER_PAGE,
                Sort.by(Sort.Direction.DESC, "recipeBoardCreatedAt")
        );

        Page<RecipeBoard> recipeBoardPage = recipeBoardRepository.findAll(pageable);

        if (recipeBoardPage.getContent().isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD);
        }

        return convertEntityPageToDTOPage(recipeBoardPage);
    }

    @Override
    public ResponseBoardDTO updateRecipeBoard(Long recipeBoardId, RecipeBoardDTO updateRecipeBoardDTO) {

        RecipeBoard recipeBoard = recipeBoardRepository.findById(recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        // 업데이트할 필드 값이 입력되었을 때만 변경
        if (updateRecipeBoardDTO.getRecipeBoardMenuName() != null) {
            recipeBoard.setRecipeBoardMenuName(updateRecipeBoardDTO.getRecipeBoardMenuName());
        }
        if (updateRecipeBoardDTO.getRecipeBoardIngredient() != null) {
            recipeBoard.setRecipeBoardIngredient(updateRecipeBoardDTO.getRecipeBoardIngredient());
        }
        if (updateRecipeBoardDTO.getRecipeBoardImage() != null) {
            recipeBoard.setRecipeBoardImage(updateRecipeBoardDTO.getRecipeBoardImage());
        }

        recipeBoard = recipeBoardRepository.save(recipeBoard);
        return modelMapper.map(recipeBoard, ResponseBoardDTO.class);
    }

    @Override
    public ResponseBoardDTO updateRecipeBoardManual(Long recipeBoardId, List<RecipeBoardManualDTO> updateRecipeBoardManualDTOs) {

        recipeBoardManualRepository.deleteByRecipeBoardRecipeBoardId(recipeBoardId);

        RecipeBoard recipeBoard = recipeBoardRepository.findById(recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        List<RecipeBoardManual> manualList = new ArrayList<>();
        for (RecipeBoardManualDTO manualDTO : updateRecipeBoardManualDTOs) {
            RecipeBoardManual newManual = modelMapper.map(manualDTO, RecipeBoardManual.class);
            newManual.setRecipeBoard(recipeBoard);
            manualList.add(newManual);
        }
        recipeBoardManualRepository.saveAll(manualList);
        return modelMapper.map(recipeBoard, ResponseBoardDTO.class);
    }

    @Override
    public ResponseBoardDTO findRecipeBoardById(Long recipeBoardId) {

        RecipeBoard recipeBoard = recipeBoardRepository.findById(recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        return modelMapper.map(recipeBoard, ResponseBoardDTO.class);
    }

    @Override
    public Page<RecipeBoardDTO> findRecipeBoardByMenuName(String recipeBoardMenuName, Integer pageNo) {
        // 페이지 번호 유효성 검사
        if (pageNo == null || pageNo < 1) {
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        }

        Pageable pageable = PageRequest.of(
                pageNo - 1,
                ELEMENTS_PER_PAGE,
                Sort.by(Sort.Direction.DESC, "recipeBoardId")
        );

        Page<RecipeBoard> recipeBoardPage = recipeBoardRepository.findByRecipeBoardMenuNameContaining(recipeBoardMenuName, pageable);

        if (recipeBoardPage.getContent().isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD);
        }

        return convertEntityPageToDTOPage(recipeBoardPage);
    }

    @Override
    public Page<RecipeBoardDTO> findRecipeBoardByUserId(Long userId, Integer pageNo) {
        // 페이지 번호 유효성 검사
        if (pageNo == null || pageNo < 1) {
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        }

        Pageable pageable = PageRequest.of(
                pageNo - 1,
                3,
                Sort.by(Sort.Direction.DESC, "recipeBoardId")
        );

        Page<RecipeBoard> recipeBoardPage = recipeBoardRepository.findByUserId(userId, pageable);
        if (recipeBoardPage.getContent().isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD);
        }
        return convertEntityPageToDTOPage(recipeBoardPage);
    }

    @Override
    public void removeRecipeBoard(Long recipeBoardId) {
        RecipeBoard existingBoard = recipeBoardRepository.findById(recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        recipeBoardRepository.delete(existingBoard);
    }

    private Page<RecipeBoardDTO> convertEntityPageToDTOPage(Page<RecipeBoard> recipeBoardPage) {
        List<RecipeBoardDTO> recipeBoardDTOList = recipeBoardPage.getContent().stream()
                .map(recipeBoard -> modelMapper.map(recipeBoard, RecipeBoardDTO.class))
                .toList();

        return new PageImpl<>(recipeBoardDTOList, recipeBoardPage.getPageable(), recipeBoardPage.getTotalElements());
    }
}

