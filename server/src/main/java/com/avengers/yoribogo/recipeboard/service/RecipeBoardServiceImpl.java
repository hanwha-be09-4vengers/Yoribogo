package com.avengers.yoribogo.RecipeBoard.service;

import com.avengers.yoribogo.RecipeBoard.domain.RecipeBoard;
import com.avengers.yoribogo.RecipeBoard.dto.RecipeBoardDTO;
import com.avengers.yoribogo.RecipeBoard.repository.RecipeBoardRepository;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class RecipeBoardServiceImpl implements RecipeBoardService {

    public final Integer ELEMENTS_PER_PAGE = 10;

    private final ModelMapper modelMapper;
    private final RecipeBoardRepository recipeBoardRepository;

    @Autowired
    public RecipeBoardServiceImpl(ModelMapper modelMapper, RecipeBoardRepository recipeBoardRepository) {
        this.modelMapper = modelMapper;
        this.recipeBoardRepository = recipeBoardRepository;
    }

    @Override
    public RecipeBoardDTO registRecipeBoard(RecipeBoardDTO registRecipeBoardDTO) {
        RecipeBoard newRecipeBoard = modelMapper.map(registRecipeBoardDTO, RecipeBoard.class);
        newRecipeBoard = recipeBoardRepository.save(newRecipeBoard);

        return modelMapper.map(newRecipeBoard, RecipeBoardDTO.class);
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
    public RecipeBoardDTO findRecipeBoardById(Long recipeBoardId) {

        RecipeBoard recipeBoard = recipeBoardRepository.findById(recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        return modelMapper.map(recipeBoard, RecipeBoardDTO.class);
    }

    private Page<RecipeBoardDTO> convertEntityPageToDTOPage(Page<RecipeBoard> recipeBoardPage) {
        List<RecipeBoardDTO> recipeBoardDTOList = recipeBoardPage.getContent().stream()
                .map(recipeBoard -> modelMapper.map(recipeBoard, RecipeBoardDTO.class))
                .toList();

        return new PageImpl<>(recipeBoardDTOList, recipeBoardPage.getPageable(), recipeBoardPage.getTotalElements());
    }
}
