package com.avengers.yoribogo.recipe.repository;

import com.avengers.yoribogo.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {
}
