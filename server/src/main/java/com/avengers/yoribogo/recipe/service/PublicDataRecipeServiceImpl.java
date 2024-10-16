package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.recipe.domain.PublicDataRecipe;
import com.avengers.yoribogo.recipe.dto.PublicDataRecipeDTO;
import com.avengers.yoribogo.recipe.repository.PublicDataRecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicDataRecipeServiceImpl implements PublicDataRecipeService {

    private final ModelMapper modelMapper;
    private final PublicDataRecipeRepository publicDataRecipeRepository;

    @Autowired
    public PublicDataRecipeServiceImpl(ModelMapper modelMapper,
                                       PublicDataRecipeRepository publicDataRecipeRepository) {
        this.modelMapper = modelMapper;
        this.publicDataRecipeRepository = publicDataRecipeRepository;
    }

    // 공공데이터 요리 레시피 등록
    @Override
    public PublicDataRecipeDTO registPublicDataRecipe(PublicDataRecipeDTO publicDataRecipeDTO) {
        PublicDataRecipe publicDataRecipe =
                modelMapper.map(publicDataRecipeDTO, PublicDataRecipe.class);
        return modelMapper.map(publicDataRecipeRepository.save(publicDataRecipe), PublicDataRecipeDTO.class);
    }

}
