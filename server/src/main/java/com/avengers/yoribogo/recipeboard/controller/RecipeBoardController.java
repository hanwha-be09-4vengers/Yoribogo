package com.avengers.yoribogo.recipeboard.controller;

import com.avengers.yoribogo.recipeboard.dto.RecipeBoardDTO;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardService;
import com.avengers.yoribogo.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe-board")
public class RecipeBoardController {

    private final RecipeBoardService recipeBoardService;

    @Autowired
    public RecipeBoardController(RecipeBoardService recipeBoardService) {
        this.recipeBoardService = recipeBoardService;
    }

    @PostMapping("/{userId}")
    public ResponseDTO<?> createRecipeBoard(
            @PathVariable Long userId,
            @RequestBody RecipeBoardDTO registRecipeBoardDTO) {
        registRecipeBoardDTO.setUserId(userId);
        RecipeBoardDTO recipeBoardDTO = recipeBoardService.registRecipeBoard(registRecipeBoardDTO);
        return ResponseDTO.ok(recipeBoardDTO);
    }

    // 페이지 번호로 나만의 레시피 전체 조회
    @GetMapping("/boards")
    public ResponseDTO<?> getRecipeBoardByPageNo(@RequestParam Integer pageNo) {
        Page<RecipeBoardDTO> recipeBoardDTOPage = recipeBoardService.findRecipeBoardByPageNo(pageNo);
        return ResponseDTO.ok(recipeBoardDTOPage);
    }

    // 나만의 레시피 게시글 단건 조회
    @GetMapping("/detail/{recipeBoardId}")
    public ResponseDTO<?> getRecipeBoardById(@PathVariable("recipeBoardId") Long recipeBoardId) {
        RecipeBoardDTO recipeBoardDTO = recipeBoardService.findRecipeBoardById(recipeBoardId);
        return ResponseDTO.ok(recipeBoardDTO);
    }
}
