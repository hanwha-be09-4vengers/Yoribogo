package com.avengers.yoribogo.recipe.repository;

import com.avengers.yoribogo.recipe.domain.AIRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIRecipeRepository extends JpaRepository<AIRecipe, Long> {
}
