package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.recipeboard.dto.RecipeBoardDTO;
import org.springframework.data.domain.Page;

public interface RecipeBoardService {

    RecipeBoardDTO registRecipeBoard(RecipeBoardDTO registRecipeBoardDTO);

    Page<RecipeBoardDTO> findRecipeBoardByPageNo(Integer pageNo);

    RecipeBoardDTO findRecipeBoardById(Long recipeBoardId);
}
