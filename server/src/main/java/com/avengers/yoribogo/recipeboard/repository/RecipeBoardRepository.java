package com.avengers.yoribogo.recipeboard.repository;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeBoardRepository extends JpaRepository<RecipeBoard, Long> {

}
