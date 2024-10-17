package com.avengers.yoribogo.recipeboard.controller;

import com.avengers.yoribogo.recipeboard.dto.RecipeBoardRecommentDTO;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardCommentServiceImpl;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardRecommentService;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardRecommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/recipe-board/{recipeBoardId}/comments/{commentId}")

public class RecipeBoardRecommentController {
    private final RecipeBoardRecommentServiceImpl recipeBoardRecommentService;



    @Autowired
    public RecipeBoardRecommentController(RecipeBoardRecommentServiceImpl recipeBoardRecommentService) {
        this.recipeBoardRecommentService = recipeBoardRecommentService;
    }


    // 대댓글 등록
    @PostMapping
    public ResponseEntity<RecipeBoardRecommentDTO> createRecomment(
            @PathVariable Long commentId,
            @RequestBody RecipeBoardRecommentDTO recommentDTO) {

        recommentDTO.setRecipeBoardCommentId(commentId);

        RecipeBoardRecommentDTO createdRecomment = recipeBoardRecommentService.createRecomment(commentId, recommentDTO);
        return new ResponseEntity<>(createdRecomment, HttpStatus.CREATED);

    }


    //     대댓글 수정
    @PutMapping({"/recomments/{recommentId}"})
    public ResponseEntity<RecipeBoardRecommentDTO> updateComment(
            @PathVariable Long recommentId,
            @RequestBody RecipeBoardRecommentDTO recommentDTO) {

        recommentDTO.setRecipeBoardCommentId(recommentId);

        RecipeBoardRecommentDTO updatedRecomment = recipeBoardRecommentService.updateRecomment(recommentId, recommentDTO);
        return new ResponseEntity<>(updatedRecomment, HttpStatus.OK);
    }


    // 대댓글 삭제
    @DeleteMapping("/recomments/{recommentId}")
    public ResponseEntity<RecipeBoardRecommentDTO> deleteRecomment(
            @PathVariable Long recommentId) {

        recipeBoardRecommentService.deleteRecomment(recommentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);


    }






}