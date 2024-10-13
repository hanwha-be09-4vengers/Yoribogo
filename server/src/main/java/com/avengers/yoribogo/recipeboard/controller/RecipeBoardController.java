package com.avengers.yoribogo.RecipeBoard.controller;

import com.avengers.yoribogo.RecipeBoard.dto.RecipeBoardDTO;
import com.avengers.yoribogo.RecipeBoard.service.RecipeBoardService;
import com.avengers.yoribogo.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipe-board")
public class RecipeBoardController {

    private final RecipeBoardService recipeBoardService;

    @Autowired
    public RecipeBoardController(RecipeBoardService recipeBoardService) {
        this.recipeBoardService = recipeBoardService;
    }

    @PostMapping
    public ResponseDTO<?> createRecipeBoard(@RequestBody RecipeBoardDTO registRecipeBoardDTO) {
        RecipeBoardDTO recipeBoardDTO = recipeBoardService.registRecipeBoard(registRecipeBoardDTO);
        return ResponseDTO.ok(recipeBoardDTO);
    }
}
