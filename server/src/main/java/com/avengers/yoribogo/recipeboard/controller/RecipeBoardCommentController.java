package com.avengers.yoribogo.recipeboard.controller;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardComment;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardCommentDTO;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardCommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-board/{recipeBoardId}/comments")
public class RecipeBoardCommentController {
    private final RecipeBoardCommentServiceImpl recipeBoardCommentService;

    @Autowired
    public RecipeBoardCommentController(RecipeBoardCommentServiceImpl recipeBoardCommentService) {
        this.recipeBoardCommentService = recipeBoardCommentService;
    }



    // 댓글 등록
    @PostMapping
    public ResponseEntity<RecipeBoardCommentDTO> createComment(
            @PathVariable Long recipeBoardId,
            @RequestBody RecipeBoardCommentDTO commentDTO){

        commentDTO.setRecipeBoardId(recipeBoardId);

        RecipeBoardCommentDTO createdComment = recipeBoardCommentService.createComment(commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);

    }



    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<RecipeBoardCommentDTO> modifyComment(
            @PathVariable Long id,
            @RequestBody RecipeBoardCommentDTO commentDTO){

        RecipeBoardCommentDTO updatedComment = recipeBoardCommentService.modifyComment(id, commentDTO);
        return new ResponseEntity<RecipeBoardCommentDTO>(updatedComment, HttpStatus.OK);
    }



    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<RecipeBoardComment> deleteComment(@PathVariable Long id){

        recipeBoardCommentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }



    // 댓글 게시글별 조회
    @GetMapping("/api/recipe-board/{recipeId}/comments")
    public ResponseEntity<List<RecipeBoardCommentDTO>> getCommentsByRecipeBoardId(@PathVariable Long recipeId){

        List<RecipeBoardCommentDTO> commentsByRecipeBoardId = recipeBoardCommentService.getCommentsByRecipeBoardId(recipeId);
        return new ResponseEntity<>(commentsByRecipeBoardId, HttpStatus.OK);
    }



    // 댓글 회원별 조회
    @GetMapping("/api/recipe-board/{userId}/comments")
    public ResponseEntity<List<RecipeBoardCommentDTO>> getCommentsByUserId(@PathVariable Long userId){

        List<RecipeBoardCommentDTO> commentsByUserId = recipeBoardCommentService.getCommentsByUserId(userId);
        return new ResponseEntity<>(commentsByUserId, HttpStatus.OK);
    }




}
