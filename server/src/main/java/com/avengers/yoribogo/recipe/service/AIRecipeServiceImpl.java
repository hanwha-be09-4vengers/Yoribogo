package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.domain.AIRecipe;
import com.avengers.yoribogo.recipe.dto.AIRecipeDTO;
import com.avengers.yoribogo.recipe.repository.AIRecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIRecipeServiceImpl implements AIRecipeService {

    private final ModelMapper modelMapper;
    private final AIRecipeRepository aiRecipeRepository;

    @Autowired
    public AIRecipeServiceImpl(ModelMapper modelMapper,
                               AIRecipeRepository aiRecipeRepository) {
        this.modelMapper = modelMapper;
        this.aiRecipeRepository = aiRecipeRepository;
    }

    // AI 요리 레시피 등록
    @Override
    public AIRecipeDTO registAIRecipe(AIRecipeDTO aiRecipeDTO) {
        AIRecipe aiRecipe = modelMapper.map(aiRecipeDTO, AIRecipe.class);
        return modelMapper.map(aiRecipeRepository.save(aiRecipe), AIRecipeDTO.class);
    }

}
