package com.avengers.yoribogo.RecipeBoard.service;

import com.avengers.yoribogo.RecipeBoard.domain.RecipeBoard;
import com.avengers.yoribogo.RecipeBoard.dto.RecipeBoardDTO;
import com.avengers.yoribogo.RecipeBoard.repository.RecipeBoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class RecipeBoardServiceImpl implements RecipeBoardService {

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
}
