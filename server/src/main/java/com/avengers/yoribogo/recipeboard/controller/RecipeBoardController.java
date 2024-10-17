package com.avengers.yoribogo.recipeboard.controller;

import com.avengers.yoribogo.recipeboard.dto.RecipeBoardDTO;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardManualDTO;
import com.avengers.yoribogo.recipeboard.dto.ResponseBoardDTO;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardService;
import com.avengers.yoribogo.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-board")
public class RecipeBoardController {

    private final RecipeBoardService recipeBoardService;

    @Autowired
    public RecipeBoardController(RecipeBoardService recipeBoardService) {
        this.recipeBoardService = recipeBoardService;
    }

    // 나만의 레시피 등록
    @PostMapping("/{userId}")
    public ResponseDTO<?> createRecipeBoard(
            @PathVariable Long userId,
            @RequestBody RecipeBoardDTO registRecipeBoardDTO) {
        // 등록된 게시글을 ResponseBoardDTO로 반환
        ResponseBoardDTO responseBoardDTO = recipeBoardService.registRecipeBoard(userId, registRecipeBoardDTO);
        return ResponseDTO.ok(responseBoardDTO);  // 조회용 DTO로 반환
    }

    // 나만의 레시피 매뉴얼 리스트 등록
    @PostMapping("/{recipeBoardId}/manuals")
    public ResponseDTO<?> createRecipeBoardManual(
            @PathVariable Long recipeBoardId,
            @RequestBody List<RecipeBoardManualDTO> registRecipeBoardManualDTOs) {
        ResponseBoardDTO responseBoardDTO = recipeBoardService.registRecipeBoardManual(recipeBoardId, registRecipeBoardManualDTOs);
        return ResponseDTO.ok(responseBoardDTO);
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

    // 요리 이름으로 게시글 전체 조회
    @GetMapping("/search")
    public ResponseDTO<?> search(@RequestParam String recipeBoardMenuName,
                                 @RequestParam Integer pageNo) {
        Page<RecipeBoardDTO> recipeBoardDTOPage = recipeBoardService.findRecipeBoardByMenuName(recipeBoardMenuName, pageNo);
        return ResponseDTO.ok(recipeBoardDTOPage);
    }

    // 게시글 수정
    @PutMapping("/update/{recipeBoardId}")
    public ResponseDTO<?> updateRecipeBoard(@PathVariable Long recipeBoardId,
                                            @RequestBody RecipeBoardDTO updateRecipeBoardDTO) {
        ResponseBoardDTO responseBoardDTO = recipeBoardService.updateRecipeBoard(recipeBoardId, updateRecipeBoardDTO);
        return ResponseDTO.ok(responseBoardDTO);
    }

    // 매뉴얼 수정
    @PostMapping("/update/{recipeBoardId}/manuals")
    public ResponseDTO<?> updateRecipeBoardManual(@PathVariable Long recipeBoardId,
                                                  @RequestBody List<RecipeBoardManualDTO> updateRecipeBoardManualDTOs) {
        ResponseBoardDTO responseBoardDTO = recipeBoardService.updateRecipeBoardManual(recipeBoardId, updateRecipeBoardManualDTOs);
        return ResponseDTO.ok(responseBoardDTO);
    }

    // 매뉴얼 삭제
    @DeleteMapping("/delete/{recipeBoardId}")
    public ResponseDTO<?> deleteRecipeBoard(@PathVariable Long recipeBoardId) {
        recipeBoardService.removeRecipeBoard(recipeBoardId);
        return ResponseDTO.ok(null);
    }

}
