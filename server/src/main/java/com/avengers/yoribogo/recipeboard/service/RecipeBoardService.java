package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.recipeboard.dto.RecipeBoardDTO;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardManualDTO;
import com.avengers.yoribogo.recipeboard.dto.ResponseBoardDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RecipeBoardService {

    ResponseBoardDTO registRecipeBoard(Long userId, RecipeBoardDTO registRecipeBoardDTO);

    Page<RecipeBoardDTO> findRecipeBoardByPageNo(Integer pageNo);

    RecipeBoardDTO findRecipeBoardById(Long recipeBoardId);

    ResponseBoardDTO registRecipeBoardManual(Long userId, List<RecipeBoardManualDTO> registRecipeBoardDTOs);

    ResponseBoardDTO updateRecipeBoard(Long recipeBoardId, RecipeBoardDTO updateRecipeBoardDTO);

    ResponseBoardDTO updateRecipeBoardManual(Long recipeBoardId, List<RecipeBoardManualDTO> updateRecipeBoardManualDTOs);
}
