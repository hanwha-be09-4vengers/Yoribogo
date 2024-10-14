package com.avengers.yoribogo.recipeboard.controller;

import com.avengers.yoribogo.recipeboard.dto.RecipeBoardManualDTO;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardManualService;
import com.avengers.yoribogo.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board-manuals")
public class RecipeBoardManualController {

    private final RecipeBoardManualService recipeBoardManualService;

    @Autowired
    public RecipeBoardManualController(RecipeBoardManualService recipeBoardManualService) {
        this.recipeBoardManualService = recipeBoardManualService;
    }

    @PostMapping
    public ResponseDTO<List<RecipeBoardManualDTO>> createRecipeBoardManual(@RequestParam("recipeBoard") Long recipeBoardId,
                                               @RequestBody List<RecipeBoardManualDTO> recipeBoardManualDTOList) {
        List<RecipeBoardManualDTO> manualDTOList =
                recipeBoardManualService.registRecipeBoardManual(recipeBoardId, recipeBoardManualDTOList);

        return ResponseDTO.ok(manualDTOList);
    }
}
