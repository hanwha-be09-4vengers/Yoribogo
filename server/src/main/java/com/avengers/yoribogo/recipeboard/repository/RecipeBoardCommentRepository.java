package com.avengers.yoribogo.recipeboard.repository;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeBoardCommentRepository extends JpaRepository<RecipeBoardComment, Long> {


    // 레시피 id로 조회
    List<RecipeBoardComment> findAllByRecipeBoardId(Long recipeId);

    // 회원 id로 조회
    List<RecipeBoardComment> findAllByUserId(Long userId);




}