package com.avengers.yoribogo.recipe.repository;

import com.avengers.yoribogo.recipe.domain.PublicDataRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicDataRecipeRepository extends JpaRepository<PublicDataRecipe, Long> {
}
