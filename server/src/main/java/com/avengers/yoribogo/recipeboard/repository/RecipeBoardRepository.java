package com.avengers.yoribogo.RecipeBoard.repository;

import com.avengers.yoribogo.RecipeBoard.domain.RecipeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeBoardRepository extends JpaRepository<RecipeBoard, Long> {

}
