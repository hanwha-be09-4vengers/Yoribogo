package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.recipeboard.dto.RecipeBoardManualDTO;

import java.util.List;

public interface RecipeBoardManualService {

    List<RecipeBoardManualDTO> registRecipeBoardManual(Long recipeBoardId, List<RecipeBoardManualDTO> recipeBoardManualDTOList);
}
