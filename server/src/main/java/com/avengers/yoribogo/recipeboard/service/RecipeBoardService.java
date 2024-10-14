package com.avengers.yoribogo.RecipeBoard.service;

import com.avengers.yoribogo.RecipeBoard.dto.RecipeBoardDTO;
import org.springframework.data.domain.Page;

public interface RecipeBoardService {

    RecipeBoardDTO registRecipeBoard(RecipeBoardDTO registRecipeBoardDTO);

    Page<RecipeBoardDTO> findRecipeBoardByPageNo(Integer pageNo);

    RecipeBoardDTO findRecipeBoardById(Long recipeBoardId);
}
