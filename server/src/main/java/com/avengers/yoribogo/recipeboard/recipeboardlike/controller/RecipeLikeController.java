package com.avengers.yoribogo.recipeboard.recipeboardlike.controller;

import com.avengers.yoribogo.recipeboard.recipeboardlike.Service.RecipeBoardLikeService;
import com.avengers.yoribogo.recipeboard.recipeboardlike.dto.LikeRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class RecipeLikeController {

    private final RecipeBoardLikeService recipeBoardLikeService;

    public RecipeLikeController(RecipeBoardLikeService recipeBoardLikeService) {
        this.recipeBoardLikeService = recipeBoardLikeService;
    }

    @PostMapping("/like")
    public ResponseEntity<String> likePost(@RequestBody LikeRequestDTO likeRequestDTO) {
        recipeBoardLikeService.likePost(likeRequestDTO);
        return ResponseEntity.ok("좋아요를 성공적으로 추가했습니다.");
    }
}
